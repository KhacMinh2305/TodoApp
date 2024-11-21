package ui.viewmodel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import data.repo.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val profileRepo : ProfileRepository) : ViewModel() {

    private val _sheetState = MutableStateFlow(false)
    val sheetState : StateFlow<Boolean> = _sheetState
}