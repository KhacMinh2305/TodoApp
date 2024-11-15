package com.example.todo.view.fragment

import  android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.R
import com.example.todo.view.activity.MainActivity
import com.example.todo.view.fragment.CreateTaskFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment(){
    private lateinit var binding: ActivityMainBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addTaskButton = view.findViewById<FloatingActionButton>(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main, CreateTaskFragment())
                .addToBackStack(null)
                .commit()
        }
    }


}