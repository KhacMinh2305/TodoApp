package ui.viewmodel
import android.util.Log
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

    /*private val _signingState = MutableStateFlow(false)
    val signingState : StateFlow<Boolean> = _signingState*/

    private val _signingState = MutableLiveData<Boolean>()
    val signingState : LiveData<Boolean> = _signingState

    private val _bottomNavState = MutableStateFlow(false)
    val bottomNavState : StateFlow<Boolean> = _bottomNavState

    private val _splashState = MutableStateFlow(true)
    val splashState : StateFlow<Boolean> = _splashState

    fun notifySplashFinished() {
        _splashState.value = false
    }

    init {
        viewModelScope.launch {
            profileRepo.checkIfUserRememberAccount().isEmpty().also {
                _signingState.value = it
                _bottomNavState.value = !it
            }
        }
    }

}