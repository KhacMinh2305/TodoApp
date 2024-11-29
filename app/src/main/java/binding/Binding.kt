package binding
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.todo.R
import config.AppConstant
import domain.DateTimeUseCase

object Binding {

    @BindingAdapter("percentText")
    @JvmStatic
    fun bindPercentText(view: TextView, percentText: Float) {
        val text = "${percentText.toInt()}%"
        view.text = text
    }

    @BindingAdapter(value = ["startTime", "endTime", "startDate", "endDate"], requireAll = true)
    @JvmStatic
    fun bindTimeText(view: TextView, startTime: String, endTime: String, startDate: String, endDate: String) {
        val dateTimeUseCase = DateTimeUseCase()
        val start = dateTimeUseCase.convertLongToDateString(startDate.toLong())
        val end = dateTimeUseCase.convertLongToDateString(endDate.toLong())
        val text = "$start : $startTime -- $end : $endTime"
        view.text = text
    }

    @BindingAdapter("taskPriority")
    @JvmStatic
    fun bindTaskPriority(view: TextView, taskPriority: Int) {
        when(taskPriority) {
            AppConstant.PRIORITY_LOW -> {
                view.setTextColor(view.resources.getColor(R.color.low, null))
                view.setText(R.string.low)
            }
            AppConstant.PRIORITY_MEDIUM -> {
                view.setTextColor(view.resources.getColor(R.color.medium, null))
                view.setText(R.string.medium)
            }
            AppConstant.PRIORITY_HIGH -> {
                view.setTextColor(view.resources.getColor(R.color.pink, null))
                view.setText(R.string.high)
            }
        }
    }

    @BindingAdapter("finishState")
    @JvmStatic
    fun bindTaskState(view: TextView, finishState: Int) {
        when(finishState) {
            AppConstant.TASK_STATE_FINISHED -> {
                view.setTextColor(view.resources.getColor(R.color.low, null))
                view.setText(R.string.finished)
            }
            AppConstant.TASK_STATE_NOT_FINISHED -> {
                view.setTextColor(view.resources.getColor(R.color.pink, null))
                view.setText(R.string.not_finished)
            }
            AppConstant.TASK_STATE_EXPIRED -> {
                view.setTextColor(view.resources.getColor(R.color.gray, null))
                view.setText(R.string.expired)
            }
        }
    }

    @BindingAdapter("backGroundPriority")
    @JvmStatic
    fun bindTaskItemBackground(view : ConstraintLayout, priority : Int) {
        when(priority) {
            AppConstant.PRIORITY_LOW -> {
                view.setBackgroundColor(view.resources.getColor(R.color.super_light_low, null))
            }
            AppConstant.PRIORITY_MEDIUM -> {
                view.setBackgroundColor(view.resources.getColor(R.color.super_light_medium, null))
            }
            AppConstant.PRIORITY_HIGH -> {
                view.setBackgroundColor(view.resources.getColor(R.color.super_light_pink, null))
            }
        }
    }

    @BindingAdapter("colorPriority")
    @JvmStatic
    fun bindTaskItemIndicator(view: View, priority: Int) {
        when(priority) {
            AppConstant.PRIORITY_LOW -> {
                view.setBackgroundColor(view.resources.getColor(R.color.low, null))
            }
            AppConstant.PRIORITY_MEDIUM -> {
                view.setBackgroundColor(view.resources.getColor(R.color.medium, null))
            }
            AppConstant.PRIORITY_HIGH -> {
                view.setBackgroundColor(view.resources.getColor(R.color.pink, null))
            }
        }
    }
}