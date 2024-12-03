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
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.todo.R
import com.example.todo.databinding.FragmentHomeBinding
import config.AppConstant
import dagger.hilt.android.AndroidEntryPoint
import ui.adapter.ProgressAdapter
import ui.adapter.TaskAdapter
import ui.custom.RecyclerViewItemDecoration
import ui.viewmodel.AppViewModel
import ui.viewmodel.HomeViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private lateinit var appViewModel: AppViewModel
    private lateinit var viewModel: HomeViewModel

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
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        navController = findNavController(requireActivity(), R.id.nav_host)
        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.viewmodel = viewModel
        initViews()
        observeStates()
        setupListeners()
        return binding.root
    }

    private fun initViews() {
        initWeekProgressViews()
        initTodayRecyclerViews()
    }

    private fun initWeekProgressViews() {
        binding.weekProgressRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.weekProgressRecyclerView.adapter = ProgressAdapter()
        binding.weekProgressRecyclerView.addItemDecoration(RecyclerViewItemDecoration(30))
    }

    private fun initTodayRecyclerViews() {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.todayTaskRecyclerView)
        binding.todayTaskRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.todayTaskRecyclerView.adapter = TaskAdapter(R.layout.task_item, onClickItem(), null)
    }

    private fun onClickItem() = { id : String ->
        appViewModel.showBottomNav(false)
        navController.navigate(R.id.action_homeFragment_to_taskDetailFragment, wrapNavigationData(id))
    }

    private fun observeStates() {
        observeLoadingHomeDataState()
        observeLoadingDataSuccessState()
        observeWeekProgressState()
        observeTodayTaskState()
        observeMessageState()
        observeUpdateState()
    }

    private fun observeUpdateState() {
        viewModel.notifyOtherScreenUpdate.observe(viewLifecycleOwner) {
            if(it) {
                appViewModel.notifyFinishedTask()
            }
        }
    }

    private fun observeMessageState() {
        viewModel.messageState.observe(viewLifecycleOwner) {
            appViewModel.receiveMessage(it)
        }
    }

    private fun wrapNavigationData(id : String) = Bundle().apply { putString(AppConstant.TASK_ID_TAG, id) }

    private fun observeLoadingHomeDataState() {
        appViewModel.loadHomeData.observe(viewLifecycleOwner) {
            if(it) viewModel.loadData()
        }
    }

    private fun observeLoadingDataSuccessState() {
        viewModel.loadingDataSuccessState.observe(viewLifecycleOwner) {
            appViewModel.notifyLoadedHomeData()
        }
    }

    private fun observeWeekProgressState() {
        viewModel.weekProgressState.observe(viewLifecycleOwner) {
            (binding.weekProgressRecyclerView.adapter as ProgressAdapter).submit(it)
        }
    }

    private fun observeTodayTaskState() {
        viewModel.todayTaskState.observe(viewLifecycleOwner) {
            (binding.todayTaskRecyclerView.adapter as TaskAdapter).submit(it)
        }
    }

    private fun setupListeners() {
        finishTask()
    }

    private fun finishTask() = binding.finishCheckBox.setOnCheckedChangeListener { button, isChecked ->
        if(isChecked) {
            viewModel.finishTask()
            button.isChecked = false
        }
    }
}