package ui.custom

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import ui.adapter.ProgressAdapter
import ui.adapter.TaskAdapter
import ui.adapter.WeekDayAdapter

class RecyclerViewItemDecoration(private val space: Int) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        when(parent.adapter) {
            is ProgressAdapter -> outRect.right = if (position == parent.adapter!!.itemCount - 1) 0 else space
            is WeekDayAdapter -> outRect.left = if (position == 0) 0 else space
            is TaskAdapter -> outRect.top = if (position == 0) 0 else space
        }
    }
}