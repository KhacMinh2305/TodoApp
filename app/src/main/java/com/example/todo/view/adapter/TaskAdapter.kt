package com.example.todo.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.model.Task

class TaskAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskName: TextView = view.findViewById(R.id.taskName)
        val startTime: TextView = view.findViewById(R.id.beginEditText)
        val endTime: TextView = view.findViewById(R.id.endEditText)
        val priority: TextView = view.findViewById(R.id.priority)
    }

    // Tạo ViewHolder và inflate layout cho từng item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.create_new_task_layout, parent, false)
        return TaskViewHolder(view)
    }

    // Gắn dữ liệu vào ViewHolder
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskName.text = task.nameTask
        holder.startTime.text = task.startTime.toString()
        holder.endTime.text = task.endTime.toString()
        holder.priority.text = task.priority
    }


    override fun getItemCount() = taskList.size
}
