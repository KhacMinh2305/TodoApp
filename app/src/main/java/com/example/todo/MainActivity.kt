package com.example.todo
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.todo.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import env_variable.AppConstant
import config.TaskTrackerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ui.viewmodel.AppViewModel
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController : NavController
    private lateinit var viewModel : AppViewModel
    private lateinit var taskTrackerService : TaskTrackerService
    private var keepSplashOnScreen = true

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as TaskTrackerService.TaskTrackerBinder
            taskTrackerService = binder.getService()
            viewModel.serviceInput.observe(this@MainActivity) {
                taskTrackerService.receiveData(it)
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    taskTrackerService.workState.collect {
                        if(it.isEmpty()) return@collect
                        viewModel.notifyTaskExpired(it)
                    }
                }
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main)
            .apply { lifecycleOwner = this@MainActivity }
        init()
        observeStates()
        setupListeners()
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
    }

    override fun onStart() {
        super.onStart()
        bindToService()
    }

    private fun init() {
        binding.bottomNavView.itemActiveIndicatorColor = ColorStateList.valueOf(Color.TRANSPARENT)
        supportFragmentManager.findFragmentById(R.id.nav_host).also {
            navController = (it as NavHostFragment).navController
            binding.bottomNavView.setupWithNavController(navController)
        }
        viewModel = ViewModelProvider(this)[AppViewModel::class.java].also { binding.viewModel = it }
    }

    private fun bindToService() {
        Intent(this, TaskTrackerService::class.java).apply {
            val bundle = Bundle().apply {
                putString("arg", "Doan Khac Minh !")
            }
            putExtras(bundle)
            bindService(this, connection, BIND_AUTO_CREATE)
        }
    }

    private fun observeStates() {
        observeThemeState()
        observeLanguageState()
        observeAuthenticationState()
        observeMessagesState()
        observeSplashState()
    }

    private fun observeThemeState() {
        viewModel.themeState.observe(this@MainActivity) {
            if(it) {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
                return@observe
            }
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            return@observe
        }
    }

    @Suppress("DEPRECATION")
    private fun setLanguage(lang : String) {
        val locale = Locale(lang)
        val resources = getResources()
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    private fun observeLanguageState() {
        viewModel.languageState.observe(this@MainActivity) {
            if(it) {
                setLanguage(AppConstant.LANG_VI)
                return@observe
            }
            setLanguage(AppConstant.LANG_EN)
        }
    }

    private fun observeAuthenticationState() {
        viewModel.signingState.observe(this@MainActivity) {
            if(it) {
                navController.navigate(R.id.action_signing_in)
                return@observe
            }
            viewModel.notifyReloadHomeData()
            viewModel.notifySplashFinished()
        }
    }

    private fun observeMessagesState() {
        viewModel.messageState.observe(this@MainActivity) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun observeSplashState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.splashState.collect {
                    keepSplashOnScreen = it
                }
            }
        }
    }

    private fun setupListeners() {
        binding.addTaskButton.setOnClickListener { navController.navigate(R.id.action_creating_task)}
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}

// Adjust the id  work to a meaningful id or same thing
// When adding, updating and deleting task , need to notify to WorkManager 