package com.example.todo.view.fragment

import android.annotation.SuppressLint
import  android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.R
import com.example.todo.view.activity.MainActivity
import com.example.todo.view.fragment.CreateTaskFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment(){
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }
}