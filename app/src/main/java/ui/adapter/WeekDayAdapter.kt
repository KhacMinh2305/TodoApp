package ui.adapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todo.R
import com.example.todo.databinding.WeekDayItemBinding
import com.example.todo.databinding.WeekRecyclerNavigationItemBinding
import config.AppConstant.Companion.BACKWARD_TEMP_DAY
import config.AppConstant.Companion.BACKWARD_TEMP_DOW
import config.AppConstant.Companion.TOWARD_TEMP_DAY
import config.AppConstant.Companion.TOWARD_TEMP_DOW
import data.model.WeekDayItem
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger

class WeekDayAdapter(private val onClickCallback : (AtomicInteger, Int) -> Unit,
    private val moveWeekClickCallback : (LocalDate, Int) -> Unit) : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        const val TOWARD = 1
        const val BACKWARD = -1
    }

    private val mAsync by lazy {
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<WeekDayItem>() {
            override fun areItemsTheSame(oldItem: WeekDayItem, newItem: WeekDayItem): Boolean {
                return oldItem.dayOfMonth == newItem.dayOfMonth
            }

            override fun areContentsTheSame(oldItem: WeekDayItem, newItem: WeekDayItem): Boolean {
                return oldItem == newItem
            }
        })
    }

    private var currentSelectedIndex = AtomicInteger(LocalDate.now().dayOfWeek.value)

    override fun getItemViewType(position: Int): Int {
        return if(position == 0 || position == mAsync.currentList.size - 1) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType == 0) return NavigationViewHolder(WeekRecyclerNavigationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return WeekDayViewHolder(WeekDayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mAsync.currentList.size
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mAsync.currentList[position]
        item.let {
            if(position == 0 || position == mAsync.currentList.size - 1) {
                bindNavigationItem(holder as NavigationViewHolder, position)
                return@let
            }
            bindDayItem(holder as WeekDayViewHolder, item, position)
        }
    }

    private fun bindNavigationItem(holder : NavigationViewHolder, position : Int) {
        holder.binding.navigateImageButton.setOnClickListener {
            val selectedDate = mAsync.currentList[currentSelectedIndex.get()]
            val localDate = LocalDate.of(selectedDate.year, selectedDate.month, selectedDate.dayOfMonth)
            val direction = if(position == 0) BACKWARD else TOWARD
            moveWeekClickCallback.invoke(localDate, direction)
        }
        if(position == 0) {
            holder.binding.navigateImageButton.setImageResource(R.drawable.ic_todo_backward)
            return
        }
        holder.binding.navigateImageButton.setImageResource(R.drawable.ic_todo_torward)
    }

    private fun bindDayItem(holder : WeekDayViewHolder, item: WeekDayItem, position : Int) {
        holder.bind(item)
        if(position == currentSelectedIndex.get()) holder.updateUiOnSelected()
        holder.itemView.setOnClickListener {
            if(currentSelectedIndex.get() == position) return@setOnClickListener
            holder.updateUiOnSelected()
            onClickCallback.invoke(currentSelectedIndex, item.dayOfMonth)
            currentSelectedIndex.set(position)
        }
    }

    fun submit(newData : List<WeekDayItem>) {
        val listItem = newData.toMutableList()
        listItem.add(0, WeekDayItem(BACKWARD_TEMP_DOW, BACKWARD_TEMP_DAY, 0, 0))
        listItem.add(WeekDayItem(TOWARD_TEMP_DOW, TOWARD_TEMP_DAY, 0, 0))
        mAsync.submitList(listItem)
    }

    class WeekDayViewHolder(val binding : WeekDayItemBinding) : ViewHolder(binding.root) {
        fun bind(item : WeekDayItem) {
            binding.day = item
        }

        fun updateUiOnSelected() {
            synchronized(this) {
                binding.container.setBackgroundResource(R.drawable.selected_day_item_drawable)
            }
        }

        fun updateUiOnDefault() {
            synchronized(this) {
                binding.container.setBackgroundResource(R.drawable.default_day_item_drawable)
            }
        }
    }

    class NavigationViewHolder(val binding : WeekRecyclerNavigationItemBinding) : ViewHolder(binding.root)
}