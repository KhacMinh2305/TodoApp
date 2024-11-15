package com.example.todo.view.fragment
import android.os.Parcelable
import android.os.Bundle
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.todo.R
import com.example.todo.data.model.Task
import com.example.todo.databinding.TaskDetailLayoutBinding
import com.example.todo.utils.FileUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskDetailsFragment : Fragment() {
    private lateinit var binding:TaskDetailLayoutBinding
    private var isEditing: Boolean = false
    private var originalTask: Task? = null
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = TaskDetailLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        val task: Task? = if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
            arguments?.getParcelable("task",Task::class.java)
        }else{
            @Suppress("Deprecation")
            arguments?.getParcelable("task")
        }
        originalTask = task?.copy()
        task?.let {
            displayTaskDetails(it)
        }
        binding.editButton.visibility = View.VISIBLE
        binding.saveButton.visibility = View.GONE
        binding.cancelButton.visibility = View.GONE
        toggleEditMode(false)
        binding.editButton.setOnClickListener{

                toggleEditMode(true)
            }

        binding.saveButton.setOnClickListener {
            task?.let {
                saveTaskUpdates(it)
            }
            toggleEditMode(false)
        }

        binding.cancelButton.setOnClickListener{
            originalTask?.let {
                displayTaskDetails(it)
            }
            toggleEditMode(false)
        }
        binding.backButton.setOnClickListener {
            // Quay lại màn hình trước đó
            requireActivity().supportFragmentManager.popBackStack()
        }
    return view
    }
    private fun displayTaskDetails(task: Task){
        binding.taskName.setText(task.nameTask)
        binding.startDate.setText(FileUtils.formatTime(task.startTime))
        binding.endDate.setText(FileUtils.formatTime(task.endTime))

        when (task.priority) {
            "High" -> binding.highPriority.isChecked = true
            "Medium" -> binding.mediumPriority.isChecked = true
            "Low" -> binding.lowPriority.isChecked = true
            else -> binding.priority.clearCheck()
        }

        binding.description.setText(task.contentTask)
    }

    private fun saveTaskUpdates(task: Task) {
        task.nameTask = binding.taskName.text.toString()
        task.startTime = FileUtils.parseTime(binding.startDate.text.toString())
        task.endTime = FileUtils.parseTime(binding.endDate.text.toString())
        task.priority = when {
            binding.highPriority.isChecked -> "High"
            binding.mediumPriority.isChecked -> "Medium"
            binding.lowPriority.isChecked -> "Low"
            else -> ""
        }
        task.contentTask = binding.description.text.toString()
    }

    private fun showDateTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            editText.setText(date)

            showTimePicker(editText)

        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val time = String.format("%02d:%02d", selectedHour, selectedMinute)
            editText.append(" $time") // Thêm thời gian vào sau ngày
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun toggleEditMode(isEditing: Boolean) {
        this.isEditing = isEditing

        // Chuyển trạng thái enabled cho các trường dữ liệu
        binding.taskName.isEnabled = isEditing
        binding.startDate.isEnabled = isEditing
        binding.endDate.isEnabled = isEditing
        binding.highPriority.isEnabled = isEditing
        binding.mediumPriority.isEnabled = isEditing
        binding.lowPriority.isEnabled = isEditing
        binding.description.isEnabled = isEditing

        // Hiển thị/ẩn nút phù hợp
        if (isEditing) {
            binding.editButton.visibility = View.GONE
            binding.saveButton.visibility = View.VISIBLE
            binding.cancelButton.visibility = View.VISIBLE
        } else {
            binding.editButton.visibility = View.VISIBLE
            binding.saveButton.visibility = View.GONE
            binding.cancelButton.visibility = View.GONE
        }
    }


}