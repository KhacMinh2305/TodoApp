package ui.viewmodel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import config.AppMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import data.repo.ProfileRepository
import data.result.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val profileRepo : ProfileRepository) : ViewModel() {

    private val _sheetState = MutableStateFlow(false)
    val sheetState : StateFlow<Boolean> = _sheetState

    private val _messageState = MutableStateFlow("")
    val messageState : StateFlow<String> = _messageState

    private val _signUpState = MutableLiveData<Boolean>()
    val signUpState : LiveData<Boolean> = _signUpState

    private fun handleResult(result : Result) {
        when(result) {
            is Result.Success -> _signUpState.value = true
            is Result.Failure -> _messageState.value = result.cause
            else -> _messageState.value = AppMessage.UNDEFINED_ERROR
        }
    }

    suspend fun signUp(username : String, password : String) {
        viewModelScope.launch {
            handleResult(profileRepo.signUp(username, password))
        }
    }

    suspend fun signIn(username : String, password : String) {
        viewModelScope.launch {
            handleResult(profileRepo.signIn(username, password))
        }
    }

}