package ui.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.TaskItemBinding
import data.local.entity.Task

class TaskAdapter(val callback: (String) -> Unit) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    companion object {
        val itemCallback = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }

    private val mAsync: AsyncListDiffer<Task> = AsyncListDiffer(this, itemCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = mAsync.currentList[position]
        item?.let { task ->
            holder.bind(task)
            holder.itemView.setOnClickListener {
                callback.invoke(task.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return mAsync.currentList.size
    }

    fun submit(data: List<Task>) {
        val displayList = mutableListOf<Task>()
        data.forEach {
            displayList.add(it)
        }
        mAsync.submitList(displayList)
    }

    class TaskViewHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task) {
            binding.task = item
        }
    }
}