package ui.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.WeekProgressItemBinding
import data.model.WeekProgressItem

class ProgressAdapter : RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>() {

        companion object {
            val itemCallback = object : DiffUtil.ItemCallback<WeekProgressItem>() {
                override fun areItemsTheSame(oldItem: WeekProgressItem, newItem: WeekProgressItem): Boolean {
                    return oldItem.progress == newItem.progress && oldItem.dayOfWeek == newItem.dayOfWeek
                }

                override fun areContentsTheSame(oldItem: WeekProgressItem, newItem: WeekProgressItem): Boolean {
                    return oldItem == newItem
                }
            }
        }

    private val mAsync : AsyncListDiffer<WeekProgressItem> = AsyncListDiffer(this, itemCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        return ProgressViewHolder(WeekProgressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        val item = mAsync.currentList[position]
        item?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return mAsync.currentList.size
    }

    fun submit(data : List<WeekProgressItem>) {
        val displayList = mutableListOf<WeekProgressItem>()
        data.forEach {
            displayList.add(it)
        }
        mAsync.submitList(displayList)
    }

    class ProgressViewHolder(private val binding : WeekProgressItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : WeekProgressItem) {
            binding.prog = item
        }
    }
}