package ui.view
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.todo.R
import com.example.todo.databinding.FragmentTaskDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import env_variable.AppConstant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ui.viewmodel.AppViewModel
import ui.viewmodel.TaskDetailViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class TaskDetailFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var taskId: String

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TaskDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private lateinit var binding: FragmentTaskDetailBinding
    private lateinit var appViewModel : AppViewModel
    private lateinit var viewModel: TaskDetailViewModel
    private lateinit var navController : NavController
    private lateinit var sheetBehavior : BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            taskId = it.getString(AppConstant.TASK_ID_TAG).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        navController = findNavController(requireActivity(), R.id.nav_host)
        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java].also {
            it.showBottomNav(false)
        }
        viewModel = ViewModelProvider(this)[TaskDetailViewModel::class.java].apply {
            loadInit(taskId)
            binding.viewmodel = this
        }
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.editTaskSheet)
        observeStates()
        setupListeners()
        return binding.root
    }

    private fun observeStates() {
        observeMessages()
        observeUpdateTaskState()
    }

    private fun observeMessages() {
        viewModel.messageState.observe(viewLifecycleOwner) {
            appViewModel.receiveMessage(it)
        }
    }

    private fun observeUpdateTaskState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updateTaskState.collect {
                appViewModel.notifyReloadHomeData()
                appViewModel.notifyReloadCalenderData(it)
                // TODO : Notify service change work
            }
        }
    }

    private fun setupListeners() {
        openSheet()
        updateTask()
        turnBack()
    }

    private fun openSheet() {
        binding.editButton.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun updateTask() {
        binding.bottomSheet.updateTaskButton.setOnClickListener {
            val taskName = binding.bottomSheet.taskNameInput.text.toString()
            val startDate = binding.bottomSheet.startDateInput.text.toString()
            val endDate = binding.bottomSheet.endDateInput.text.toString()
            val beginTime = binding.bottomSheet.beginTimeInput.text.toString()
            val endTime = binding.bottomSheet.endTimeInput.text.toString()
            val description = binding.bottomSheet.descriptionInput.text.toString()
            viewModel.updateTask(taskName, startDate, endDate, beginTime, endTime, description)
        }
    }

    private fun turnBack() {
        binding.turnBackButton.setOnClickListener {
            appViewModel.showBottomNav(true)
            navController.navigateUp()
        }
    }
}