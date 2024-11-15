package com.example.todo.view.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.activity.viewModels
import com.example.todo.R
import com.example.todo.data.model.Task
import com.example.todo.data.database.TaskDao
import com.example.todo.data.repository.TaskRepository
import java.text.SimpleDateFormat
import java.util.*

class CreateTaskFragment : Fragment() {

    private lateinit var backButton: ImageButton
    private lateinit var taskName: EditText
    private lateinit var startDate: EditText
    private lateinit var endDate: EditText
    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText: EditText
    private lateinit var description: EditText
    private lateinit var priorityGroup: RadioGroup
    private val calendar = Calendar.getInstance()

    @SuppressLint("DefaultLocale")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.create_new_task_layout, container, false)

        // Khởi tạo các thành phần UI
        backButton = rootView.findViewById(R.id.backButton)
        taskName = rootView.findViewById(R.id.taskName)
        startDate = rootView.findViewById(R.id.startDate)
        endDate = rootView.findViewById(R.id.endDate)
        description = rootView.findViewById(R.id.description)
        priorityGroup = rootView.findViewById(R.id.priorityRadioGroup)
        startTimeEditText = rootView.findViewById(R.id.beginEditText)
        endTimeEditText = rootView.findViewById(R.id.endEditText)

        // Xử lý sự kiện khi nhấn nút quay lại
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Hiển thị DatePicker khi người dùng nhấn vào trường ngày bắt đầu
        startDate.setOnClickListener {
            showDatePickerDialog { date -> startDate.setText(date) }
        }

        // Hiển thị DatePicker khi người dùng nhấn vào trường ngày kết thúc
        endDate.setOnClickListener {
            showDatePickerDialog { date -> endDate.setText(date) }
        }

        // Hiển thị TimePicker khi người dùng nhấn vào trường giờ bắt đầu
        startTimeEditText.setOnClickListener {
            showTimePicker { hour, minute -> startTimeEditText.setText(String.format("%02d:%02d", hour, minute)) }
        }

        // Hiển thị TimePicker khi người dùng nhấn vào trường giờ kết thúc
        endTimeEditText.setOnClickListener {
            showTimePicker { hour, minute -> endTimeEditText.setText(String.format("%02d:%02d", hour, minute)) }
        }

        // Lắng nghe sự kiện nhấn nút tạo công việc
        val createTaskButton = rootView.findViewById<Button>(R.id.createTaskButton)
        createTaskButton.setOnClickListener {
            val selectedPriorityId = priorityGroup.checkedRadioButtonId
            val selectedPriority: String = when (selectedPriorityId) {
                R.id.highPriority -> "High"
                R.id.mediumPriority -> "Medium"
                R.id.lowPriority -> "Low"
                else -> {
                    Toast.makeText(context, "Please select a priority", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            if (taskName.text.isBlank() || startDate.text.isBlank() || endDate.text.isBlank()) {
                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Chuyển đổi ngày và giờ từ EditText thành Long
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDateLong = dateFormat.parse(startDate.text.toString())?.time ?: 0L
            val endDateLong = dateFormat.parse(endDate.text.toString())?.time ?: 0L

            // Tạo task mới (Chưa lưu vào cơ sở dữ liệu)
            val task = Task(
                idTask = 0,
                nameTask = taskName.text.toString(),
                contentTask = description.text.toString(),
                startTime = startDate.text.toString(),
                endTime = endDate.text.toString(),
                priority = selectedPriority
            )

            // Thông báo thành công
            Toast.makeText(context, "Task created successfully", Toast.LENGTH_SHORT).show()

            // Xóa dữ liệu khỏi các trường nhập liệu sau khi tạo công việc
            taskName.text.clear()
            startDate.text.clear()
            endDate.text.clear()
            description.text.clear()
            priorityGroup.clearCheck()
        }

        return rootView
    }

    // Hàm để hiển thị DatePickerDialog
    private fun showDatePickerDialog(onDateSet: (String) -> Unit) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                onDateSet(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    // Hàm để hiển thị TimePickerDialog
    private fun showTimePicker(onTimeSet: (hour: Int, minute: Int) -> Unit) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            onTimeSet(selectedHour, selectedMinute)
        }, hour, minute, true).show()
    }
}

