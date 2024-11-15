package com.example.todo.view.adapter
import com.example.todo.utils.FileUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.data.model.Task
import com.example.todo.databinding.CalendarTaskItemBinding
import com.example.todo.view.fragment.ListTaskFragment

class ListTaskAdapter(private val tasks: List<Task>, private val onItemClick: (Task) -> Unit) : RecyclerView.Adapter<ListTaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = CalendarTaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // Bind data to the view
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    inner class TaskViewHolder(private val binding: CalendarTaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.name.text = task.nameTask
            binding.timestart.text = FileUtils.formatTime(task.startTime)
            binding.timeend.text = FileUtils.formatTime(task.endTime)
            binding.description.text = task.contentTask
            binding.priority.text = task.priority
            binding.root.setOnClickListener{
              onItemClick(task)

            }

        }
    }
}
