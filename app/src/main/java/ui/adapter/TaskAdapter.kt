package ui.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todo.R
import com.example.todo.databinding.CalendarTaskItemBinding
import com.example.todo.databinding.TaskItemBinding
import data.local.entity.Task
import domain.DateTimeUseCase

class TaskAdapter(val layout : Int, val callback: (String) -> Unit) : RecyclerView.Adapter<ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(layout == R.layout.task_item) {
            return TaskViewHolder(TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
        return CalenderTaskViewHolder(CalendarTaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mAsync.currentList[position]
        item?.let { task ->
            if(layout == R.layout.task_item) {
                bindDataForTaskItemLayout(holder as TaskViewHolder, item)
                return
            }
            bindDataForCalenderTaskLayout(holder as CalenderTaskViewHolder, item)
        }
    }

    private fun bindDataForTaskItemLayout(holder: TaskViewHolder, item : Task) {
        holder.bind(item)
        holder.itemView.setOnClickListener {
            callback.invoke(item.id)
        }
    }

    private fun bindDataForCalenderTaskLayout(holder: CalenderTaskViewHolder, item : Task) {
        holder.bind(item)
        holder.itemView.setOnClickListener {
            callback.invoke(item.id)
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

    class TaskViewHolder(private val binding: TaskItemBinding) : ViewHolder(binding.root) {
        fun bind(item: Task) {
            binding.task = item
        }
    }

    class CalenderTaskViewHolder(private val binding: CalendarTaskItemBinding) : ViewHolder(binding.root) {
        fun bind(item: Task) {
            binding.task = item
            binding.dateTime = DateTimeUseCase()
        }
    }
}