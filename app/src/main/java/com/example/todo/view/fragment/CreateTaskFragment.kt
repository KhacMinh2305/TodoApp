package com.example.todo.view.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.activity.viewModels
import com.example.todo.R
import com.example.todo.data.model.Task
import com.example.todo.data.database.TaskDao
import com.example.todo.data.database.TaskRoomDatabaseClass

import com.example.todo.data.repository.TaskRepository
import java.text.SimpleDateFormat
import kotlinx.coroutines.*

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

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.create_new_task_layout, container, false)
        backButton = rootView.findViewById(R.id.backButton)
        taskName = rootView.findViewById(R.id.taskName)
        startDate = rootView.findViewById(R.id.startDate)
        endDate = rootView.findViewById(R.id.endDate)
        description = rootView.findViewById(R.id.description)
        priorityGroup = rootView.findViewById(R.id.priorityRadioGroup)
        startTimeEditText = rootView.findViewById(R.id.beginEditText)
        endTimeEditText = rootView.findViewById(R.id.endEditText)

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        startDate.setOnClickListener {
            showDatePickerDialog { date -> startDate.setText(date) }
        }

        endDate.setOnClickListener {
            showDatePickerDialog { date -> endDate.setText(date) }
        }

        startTimeEditText.setOnClickListener {
            showTimePicker { hour, minute -> startTimeEditText.setText(String.format("%02d:%02d", hour, minute)) }
        }

        endTimeEditText.setOnClickListener {
            showTimePicker { hour, minute -> endTimeEditText.setText(String.format("%02d:%02d", hour, minute)) }
        }

        val createTaskButton = rootView.findViewById<Button>(R.id.createTaskButton)
        createTaskButton.setOnClickListener {
            try {
                val taskNameadd = taskName.text.toString()
                val startDateStr = startDate.text.toString()
                val endDateStr = endDate.text.toString()
                val descriptionStr = description.text.toString()

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

                // Kiểm tra các trường nhập liệu
                if (taskNameadd.isBlank() || startDateStr.isBlank() || endDateStr.isBlank()) {
                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Chuyển đổi ngày từ String sang Long (timestamp)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val startDateLong = dateFormat.parse(startDateStr)?.time ?: 0L
                val endDateLong = dateFormat.parse(endDateStr)?.time ?: 0L

                // Tạo task mới
                val task = Task(
                    nameTask = taskNameadd,
                    contentTask = descriptionStr,
                    startTime = startDateLong,
                    endTime = endDateLong,
                    priority = selectedPriority
                )

                // Thêm task vào cơ sở dữ liệu
                val taskDao = TaskRoomDatabaseClass.getDatabase(requireContext()).taskDao()
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) {
                        try {
                            taskDao.addTask(task)
                        } catch (e: Exception) {
                            // Xử lý lỗi nếu có khi insert vào database
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Error saving task: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    // Thông báo khi thêm thành công
                    Toast.makeText(context, "Task created successfully", Toast.LENGTH_SHORT).show()
                }

                // Xóa dữ liệu khỏi các trường nhập liệu sau khi tạo công việc
                taskName.text.clear()
                startDate.text.clear()
                endDate.text.clear()
                description.text.clear()
                priorityGroup.clearCheck()

            } catch (e: Exception) {
                // Xử lý các lỗi trong quá trình tạo task
                Toast.makeText(context, "Error creating task: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }


        return rootView
    }

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

    private fun showTimePicker(onTimeSet: (hour: Int, minute: Int) -> Unit) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            onTimeSet(selectedHour, selectedMinute)
        }, hour, minute, true).show()
    }

}

