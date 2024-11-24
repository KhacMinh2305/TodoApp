package ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.repo.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val profileRepo : ProfileRepository) : ViewModel() {

    private val _signingState = MutableLiveData<Boolean>()
    val signingState : LiveData<Boolean> = _signingState

    private val _bottomNavState = MutableStateFlow(false)
    val bottomNavState : StateFlow<Boolean> = _bottomNavState

    private val _messageState = MutableLiveData<String>()
    val messageState : LiveData<String> = _messageState

    private val _splashState = MutableStateFlow(true)
    val splashState : StateFlow<Boolean> = _splashState

    fun notifySplashFinished() {
        _splashState.value = false
    }

    fun toggleBottomNav() {
        _bottomNavState.value = !_bottomNavState.value
    }

    init {
        viewModelScope.launch {
            val id = profileRepo.checkIfUserRememberAccount()
            id.isEmpty().also {
                profileRepo.loadUserIfRemember(id)
                _signingState.value = it
                _bottomNavState.value = !it
            }
        }
    }

    fun receiveMessage(message : String) {
        message.isNotEmpty().also {
            _messageState.value = message
        }
    }
}