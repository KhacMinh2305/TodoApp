package ui.view
import android.content.Intent.getIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todo.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import ui.viewmodel.AppViewModel
import ui.viewmodel.SettingViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@AndroidEntryPoint
class SettingFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentSettingBinding
    private lateinit var appViewModel : AppViewModel
    private lateinit var viewModel : SettingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        viewModel = ViewModelProvider(this)[SettingViewModel::class.java]
        observeStates()
        return binding.root
    }

    private fun observeStates() {
        appViewModel.themeState.observe(viewLifecycleOwner) {
            if(it) {
                binding.themeSwitch.isChecked = true
            }
        }

        appViewModel.languageState.observe(viewLifecycleOwner) {
            if(it) {
                binding.languageSwitch.isChecked = true
            }
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            appViewModel.changeTheme(isChecked)
        }
        binding.languageSwitch.setOnCheckedChangeListener{ _, isChecked ->
            appViewModel.changeLanguage(isChecked) }
    }
}