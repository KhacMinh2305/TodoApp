package com.example.todo.view.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.todo.R
import com.example.todo.base.BaseActivity
import com.example.todo.data.database.TaskRoomDatabaseClass
import com.example.todo.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.todo.view.fragment.CreateTaskFragment
import com.example.todo.view.fragment.HomeFragment
import com.example.todo.view.fragment.ListTaskFragment
import com.example.todo.view.fragment.SettingsFragment
import com.example.todo.view.fragment.UserProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.rxjava3.disposables.Disposable

class MainActivity : BaseActivity() {
    var homeFragment = HomeFragment()
    private var disposable: Disposable? = null
    private lateinit var binding: ActivityMainBinding

    private val taskDatabase by lazy { TaskRoomDatabaseClass.getDatabase(this).taskDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gán Fragment HomeFragment vào container
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, homeFragment)
            commit()
        }

        // Xử lý sự kiện cho FloatingActionButton
        val addTaskButton = binding.addTaskButton
        addTaskButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CreateTaskFragment())
                .addToBackStack(null)
                .commit()
        }

        // Xử lý sự kiện cho BottomNavigationView
        val bottomNavigationView = binding.bottomNavView
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_nav_graph -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, homeFragment)
                        .commit()
                    true
                }
                R.id.calendar_nav_graph -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ListTaskFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.profile_nav_graph -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, UserProfileFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.setting_nav_graph -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, SettingsFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}
