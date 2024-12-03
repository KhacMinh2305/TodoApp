package ui.view
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.databinding.FragmentCalendarBinding
import config.AppConstant
import dagger.hilt.android.AndroidEntryPoint
import data.local.entity.Task
import kotlinx.coroutines.launch
import ui.adapter.TaskAdapter
import ui.adapter.WeekDayAdapter
import ui.custom.RecyclerViewItemDecoration
import ui.viewmodel.AppViewModel
import ui.viewmodel.CalendarViewModel
import java.time.LocalDate

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private lateinit var binding : FragmentCalendarBinding
    private lateinit var appViewModel : AppViewModel
    private lateinit var viewModel : CalendarViewModel
    private lateinit var navController : NavController

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
        binding = FragmentCalendarBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        navController = findNavController(requireActivity(), R.id.nav_host)
        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        viewModel = ViewModelProvider(this)[CalendarViewModel::class.java]
        initViews()
        observeStates()
        return binding.root
    }

    private fun initViews() {
        initWeekDayRecyclerView()
        initTaskRecyclerView()
    }

    private fun initWeekDayRecyclerView() {
        binding.weekDayRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.weekDayRecyclerView.addItemDecoration(RecyclerViewItemDecoration(40))
        binding.weekDayRecyclerView.adapter = WeekDayAdapter(viewModel._currentSelectedDateIndex, onClickWeekDay(), moveWeek())
    }

    private fun initTaskRecyclerView() {
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.taskRecyclerView.addItemDecoration(RecyclerViewItemDecoration(30))
        binding.taskRecyclerView.adapter = TaskAdapter(R.layout.calendar_task_item, navigateOnClick(), onClickDelete())
    }

    private fun wrapDataForNavigation(id : String) = Bundle().apply {
        putString(AppConstant.TASK_ID_TAG, id)
    }

    private fun onClickWeekDay() = { oldPosition: Int, newPosition : Int, date: LocalDate ->
        resetOldUiOn(oldPosition)
        viewModel.loadTasks(date)
        viewModel._currentSelectedDateIndex = newPosition
        viewModel._currentSelectedDate = date
    }

    private fun moveWeek() = {date : LocalDate, direction : Int ->
        val weekDayOnNextWeek = date.plusWeeks(direction.toLong())
        viewModel.loadWeekDays(weekDayOnNextWeek)
        viewModel.loadTasks(weekDayOnNextWeek)
    }

    private fun resetOldUiOn(oldPosition : Int) {
        val viewHolder = binding.weekDayRecyclerView.findViewHolderForAdapterPosition(oldPosition)
        viewHolder?.let {
            (it as WeekDayAdapter.WeekDayViewHolder).updateUiOnDefault()
        }
    }

    private fun navigateOnClick() = { id : String ->
        navController.navigate(R.id.action_calendarFragment_to_taskDetailFragment,
            wrapDataForNavigation(id))
    }

    private fun onClickDelete() = { task : Task ->
        viewModel.deleteTask(task)
    }

    private fun observeStates() {
        observeWeekState()
        observeTasksState()
        observeMessageState()
        observeReloadOnFinishTaskState()
        observeReloadOnAddingTaskState()
        observeDeleteTaskState()
    }

    private fun collectData(func : suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            func.invoke()
        }
    }

    private fun observeWeekState() = collectData {
        viewModel.weekDaysState.collect {
            (binding.weekDayRecyclerView.adapter as WeekDayAdapter).submit(it)
        }
    }

    private fun observeTasksState() = collectData {
        viewModel.taskState.collect {
            (binding.taskRecyclerView.adapter as TaskAdapter).submit(it)
        }
    }

    private fun observeMessageState() {
        viewModel.messageState.observe(viewLifecycleOwner) {
            appViewModel.receiveMessage(it)
        }
    }

    private fun observeReloadOnFinishTaskState() {
        appViewModel.updateOnFinishTaskState.observe(viewLifecycleOwner) {
            val isToday = (binding.weekDayRecyclerView.adapter as WeekDayAdapter)
                .getCurrentDay()?.isEqual(LocalDate.now()) ?: false
            if(isToday) {
                viewModel.loadTasks(LocalDate.now())
            }
        }
    }

    private fun observeReloadOnAddingTaskState() {
        appViewModel.reLoadCalenderData.observe(viewLifecycleOwner) {
            if(!it.contains(viewModel._currentSelectedDate)) return@observe
            viewModel.loadTasks(viewModel._currentSelectedDate)
        }
    }

    private fun observeDeleteTaskState() {
        viewModel.deleteTaskState.observe(viewLifecycleOwner) {
            if(it) {
                appViewModel.notifyReloadHomeData()
            }
        }
    }
}