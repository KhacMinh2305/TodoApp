package com.example.todo.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.todo.view.fragment.CreateTaskFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addTaskButton = findViewById<FloatingActionButton>(R.id.addTaskButton)
        // Thiết lập sự kiện
        addTaskButton.setOnClickListener {
            val fragment = CreateTaskFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
