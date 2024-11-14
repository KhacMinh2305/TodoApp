package com.example.todo.view.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.todo.R

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
            showDatePickerDialog { date ->
                startDate.setText(date)
            }
        }

        endDate.setOnClickListener {
            showDatePickerDialog { date ->
                endDate.setText(date)
            }
        }
        // Đặt sự kiện onClick cho trường thời gian bắt đầu
        startTimeEditText.setOnClickListener {
            showTimePicker { hour, minute ->
                startTimeEditText.setText(String.format("%02d:%02d", hour, minute))
            }
        }

        // Đặt sự kiện onClick cho trường thời gian kết thúc
        endTimeEditText.setOnClickListener {
            showTimePicker { hour, minute ->
                endTimeEditText.setText(String.format("%02d:%02d", hour, minute))
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
         val calendar = Calendar.getInstance()
         val hour = calendar.get(Calendar.HOUR_OF_DAY)
         val minute = calendar.get(Calendar.MINUTE)

         TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
             onTimeSet(selectedHour, selectedMinute)
         }, hour, minute, true).show()
     }
}
