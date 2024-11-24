package ui.view
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.todo.R
import com.example.todo.databinding.FragmentSignInBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import config.AppMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ui.viewmodel.AppViewModel
import ui.viewmodel.AuthenticationViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class SignInFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentSignInBinding
    private lateinit var appViewModel: AppViewModel
    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var navController: NavController
    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>

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
        binding = FragmentSignInBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        navController = findNavController(requireActivity(), R.id.nav_host)
        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        viewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java].apply {
            binding.viewModel = this
            binding.bottomSheet.viewModel = this
        }
        initViews()
        observeStates()
        setupListeners()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        appViewModel.notifySplashFinished()
    }

    private fun initViews() {

        appViewModel.toggleBottomNav()
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.signUpSheet)
    }

    private fun observeStates() {
        observeSheetState()
        observeMessageState()
        observeSigningUpState()
    }

    private fun observeSheetState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sheetState.collect {
                    if(it) sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
    }

    private fun observeMessageState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messageState.collect {
                if(it.isNotEmpty()) {
                    delegateMessage(it)
                }
            }
        }
    }

    private fun observeSigningUpState() {
        viewModel.signUpState.observe(viewLifecycleOwner) {
            appViewModel.toggleBottomNav()
            navController.navigateUp()
        }
    }

    private fun setupListeners() {
        binding.openSignUpSheetButton.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.bottomSheet.closeSheet.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.bottomSheet.signUpButton.setOnClickListener { signUp() }
        binding.loginButton.setOnClickListener { signIn() }
    }

    private fun authenticate(username : String, password : String, authFun : suspend (String, String) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            if(username.isEmpty() || password.isEmpty()) {
                delegateMessage(AppMessage.EMPTY_AUTHENTICATION_FIELD)
                return@launch
            }
            authFun(username, password)
        }
    }

    private fun signUp() {
        val username = binding.bottomSheet.userName.text.toString()
        val password = binding.bottomSheet.password.text.toString()
        authenticate(username, password, viewModel::signUp)
    }

    private fun signIn() {
        val username = binding.userName.text.toString()
        val password = binding.password.text.toString()
        authenticate(username, password, viewModel::signIn)
    }

    private fun delegateMessage(message : String) {
        appViewModel.receiveMessage(message)
    }
}