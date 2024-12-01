package ui.view
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.databinding.FragmentCalendarBinding
import dagger.hilt.android.AndroidEntryPoint
import ui.adapter.TaskAdapter
import ui.adapter.WeekDayAdapter
import ui.custom.RecyclerViewItemDecoration
import ui.viewmodel.AppViewModel
import ui.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger

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
        binding.weekDayRecyclerView.adapter = WeekDayAdapter(onClickWeekDay(), moveWeek())
    }

    private fun initTaskRecyclerView() {
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.taskRecyclerView.addItemDecoration(RecyclerViewItemDecoration(30))
        binding.taskRecyclerView.adapter = TaskAdapter(R.layout.calendar_task_item) {
            println("Move to task detail fragment : $it")
        }
    }

    private fun onClickWeekDay() = { oldPosition: AtomicInteger, dayOfMonth: Int ->
        resetOldUiOn(oldPosition)
    }

    private fun moveWeek() = {date : LocalDate, direction : Int ->
        viewModel.loadWeekDays(date.plusWeeks(direction.toLong()))
    }

    private fun resetOldUiOn(oldPosition : AtomicInteger) {
        val viewHolder = binding.weekDayRecyclerView.findViewHolderForAdapterPosition(oldPosition.get())
        viewHolder?.let {
            (it as WeekDayAdapter.WeekDayViewHolder).updateUiOnDefault()
        }
    }

    private fun observeStates() {
        observeWeekState()
        observeTasksState()
    }

    private fun observeWeekState() {
        viewModel.weekDaysState.observe(viewLifecycleOwner) {
            (binding.weekDayRecyclerView.adapter as WeekDayAdapter).submit(it)
        }
    }

    private fun observeTasksState() {
        viewModel.taskState.observe(viewLifecycleOwner) {
            (binding.taskRecyclerView.adapter as TaskAdapter).submit(it)
        }
    }

}