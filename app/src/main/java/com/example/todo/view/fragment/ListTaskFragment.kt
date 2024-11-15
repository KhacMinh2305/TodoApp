package com.example.todo.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.todo.view.adapter.ListTaskAdapter
import com.example.todo.R
import com.example.todo.data.TaskDataSource
import com.example.todo.data.model.Task
import com.example.todo.databinding.CalendarTaskItemBinding

class ListTaskFragment:Fragment() {
    private lateinit var recyclerView: RecyclerView
    private  lateinit var taskAdapter: ListTaskAdapter
    private val taskList: MutableList<Task> = TaskDataSource.sampleTasks.toMutableList()
    private lateinit var backButton: ImageButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val  view = inflater.inflate(R.layout.list_task,container,false)
        recyclerView = view.findViewById(R.id.recyclerViewtask)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        taskAdapter = ListTaskAdapter(taskList)
        recyclerView.adapter = taskAdapter
        backButton = view.findViewById(R.id.backButton2)
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        return view
    }
}