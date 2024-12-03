package ui.view
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.todo.R
import com.example.todo.databinding.FragmentCreatingTaskBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import config.AppConstant
import dagger.hilt.android.AndroidEntryPoint
import domain.DateTimeUseCase
import ui.viewmodel.AppViewModel
import ui.viewmodel.CreatingTaskViewModel
import java.time.LocalDateTime

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "CREATING_TASK"

@AndroidEntryPoint
class CreatingTaskFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreatingTaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentCreatingTaskBinding
    private lateinit var navController: NavController
    private lateinit var appViewModel : AppViewModel
    private lateinit var viewModel : CreatingTaskViewModel
    private lateinit var datePicker : MaterialDatePicker<Pair<Long, Long>>
    private lateinit var timePicker :  MaterialTimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatingTaskBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        navController = findNavController(requireActivity(), R.id.nav_host)
        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        viewModel = ViewModelProvider(this)[CreatingTaskViewModel::class.java]
        setUpDatePicker()
        setUpTimePicker()
        observeStates()
        setUpListeners()
        return binding.root
    }

    private fun setUpDatePicker() {
        val constrain = CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())
        datePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Date Range")
            .setCalendarConstraints(constrain.build())
            .build()
    }

    private fun setUpTimePicker() {
        timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(LocalDateTime.now().hour)
                .setMinute(LocalDateTime.now().minute)
                .setTitleText("Select Time")
                .build()
    }

    private fun observeStates() {
        appViewModel.showBottomNav(false)
        observeMessages()
        observeAddingTaskState()
        observeUpdateCalenderState()
    }

    private fun observeMessages() {
        viewModel.messageState.observe(viewLifecycleOwner) {
            appViewModel.receiveMessage(it)
        }
    }

    private fun observeAddingTaskState() {
        viewModel.addingTaskState.observe(viewLifecycleOwner) {
            appViewModel.notifyReloadHomeData()
        }
    }

    private fun observeUpdateCalenderState() {
        viewModel.updateCalenderState.observe(viewLifecycleOwner) {
            appViewModel.notifyReloadCalenderData(it)
        }
    }

    private fun setUpListeners() {
        showDatePicker()
        showTimePicker()
        displaySelectedDate()
        displaySelectedTime()
        goBack()
        addNewTask()
    }

    private fun showDatePicker() {
        binding.chooseDateImageButton.setOnClickListener {
            datePicker.show(parentFragmentManager, TAG)
        }
    }

    private fun showTimePicker() {
        binding.chooseTimeImageButton.setOnClickListener {
            timePicker.show(parentFragmentManager, TAG)
        }
    }

    private fun displaySelectedDate() {
        datePicker.addOnPositiveButtonClickListener {
            val startDate = it.first
            val endDate = it.second
            val dateTimeUseCase = DateTimeUseCase()
            binding.startDate.setText(dateTimeUseCase.convertLongToDateString(startDate))
            binding.endDate.setText(dateTimeUseCase.convertLongToDateString(endDate))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displaySelectedTime() {
        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            val displayHour = if (hour < 10) "0$hour" else hour
            val displayMinute = if (minute < 10) "0$minute" else minute
            val displayTime = "$displayHour:$displayMinute"
            if (binding.endEditText.isFocused
                || binding.endEditText.text.isEmpty() && binding.beginEditText.text.isNotEmpty() && !binding.beginEditText.isFocused) {
                binding.endEditText.setText(displayTime)
                return@addOnPositiveButtonClickListener
            }
            binding.beginEditText.setText(displayTime)
        }
    }

    private fun addTask() {
        val taskName = binding.taskName.text.toString()
        val startDate = binding.startDate.text.toString()
        val endDate = binding.endDate.text.toString()
        val beginTime = binding.beginEditText.text.toString()
        val endTime = binding.endEditText.text.toString()
        val description = binding.description.text.toString()
        val priority = when (binding.priorityRadioGroup.checkedRadioButtonId) {
            R.id.highPriority -> AppConstant.PRIORITY_HIGH
            R.id.mediumPriority -> AppConstant.PRIORITY_MEDIUM
            R.id.lowPriority -> AppConstant.PRIORITY_LOW
            else -> 0
        }
        viewModel.addTask(taskName, startDate, endDate, beginTime, endTime, description, priority)
    }

    private fun addNewTask() {
        binding.createTaskButton.setOnClickListener {
            addTask()
        }
    }

    private fun goBack() {
        binding.backButton.setOnClickListener {
            appViewModel.showBottomNav(true)
            navController.navigateUp()
        }
    }
}