package ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import config.AppConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import data.repo.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
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

    private val _themeState = MutableLiveData<Boolean>()
    val themeState : LiveData<Boolean> = _themeState

    private val _languageState = MutableLiveData<Boolean>()
    val languageState : LiveData<Boolean> = _languageState

    private val _loadHomeData = MutableLiveData<Boolean>()
    val loadHomeData : LiveData<Boolean> = _loadHomeData

    private val _updateOnFinishTaskState = MutableLiveData<Boolean>()
    val updateOnFinishTaskState : LiveData<Boolean> = _updateOnFinishTaskState

    private val _reLoadCalenderData = MutableLiveData<LocalDate>()
    val reLoadCalenderData : LiveData<LocalDate> = _reLoadCalenderData

    init {
        viewModelScope.launch {
            if(profileRepo.getUiMode() == AppConstant.MODE_DARK) _themeState.value = true

            if(profileRepo.getLangMode() == AppConstant.LANG_VI) _languageState.value = true

            val id = profileRepo.checkIfUserRememberAccount()
            id.isEmpty().also {
                if(!it) profileRepo.loadUserIfRemember(id)
                _signingState.value = it
                _bottomNavState.value = !it
            }
        }
    }

    fun changeTheme(changed : Boolean) {
        _themeState.value = changed
        viewModelScope.launch {
            profileRepo.changeUiMode(if(changed) AppConstant.MODE_DARK else AppConstant.MODE_LIGHT)
        }
    }

    fun changeLanguage(changed: Boolean) {
        _languageState.value = changed
        viewModelScope.launch {
            profileRepo.changeLanguage(if(changed) AppConstant.LANG_VI else AppConstant.LANG_EN)
        }
    }

    fun notifyReloadHomeData() {
        _loadHomeData.value = true
    }

    fun notifyLoadedHomeData() {
        _loadHomeData.value = false
    }

    fun notifyReloadCalenderData(dateToReload : LocalDate) {
        _reLoadCalenderData.value = dateToReload
    }

    fun notifySplashFinished() {
        _splashState.value = false
    }

    fun notifyFinishedTask() {
        _updateOnFinishTaskState.value = true
    }

    fun showBottomNav( visibility : Boolean) {
        _bottomNavState.value = visibility
    }

    fun receiveMessage(message : String) {
        message.isNotEmpty().also {
            _messageState.value = message
        }
    }
}