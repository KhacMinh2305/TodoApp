package com.example.todo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.todo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import ui.viewmodel.AppViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController : NavController
    private lateinit var viewModel : AppViewModel

    // Do not change thís
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main)
            .apply { lifecycleOwner = this@MainActivity }
        supportFragmentManager.findFragmentById(R.id.nav_host).also {
            navController = (it as NavHostFragment).navController
            binding.bottomNavView.setupWithNavController(navController)
        }
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        setupListeners()
    }

    private fun setupListeners() {
        binding.addTaskButton.setOnClickListener { navController.navigate(R.id.action_creating_task)}
    }
}


// Each Screen (Fragment) has its own viewmodel that work as a bridge between UI components and data components
// Man hinh Calenda su dung week recycler view cho tung tuan , ben duoi su dung viewPager cho tung ngay trong tuan