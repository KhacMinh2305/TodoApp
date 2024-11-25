package ui.view
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.todo.R
import com.example.todo.databinding.FragmentCreatingTaskBinding
import dagger.hilt.android.AndroidEntryPoint
import ui.viewmodel.AppViewModel
import ui.viewmodel.CreatingTaskViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
        observeStates()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setUpListeners()
    }

    private fun observeStates() {
        appViewModel.showBottomNav(false)
    }

    private fun setUpListeners() {
        goBack()
    }

    private fun goBack() {
        binding.backButton.setOnClickListener {
            appViewModel.showBottomNav(true)
            navController.popBackStack()
        }
    }

}