package ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import config.AppMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import data.repo.ProfileRepository
import data.result.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepo : ProfileRepository) : ViewModel() {

    private val _signingOutState = MutableLiveData<Boolean>()
    val signingOutState : LiveData<Boolean> = _signingOutState

    private val _messageState = MutableLiveData<String>()
    val messageState : LiveData<String> = _messageState

    fun signOut() {
        viewModelScope.launch {
            when(profileRepo.signOut()) {
                is Result.Success -> _signingOutState.value = true
                else -> _messageState.value = AppMessage.UNDEFINED_ERROR
            }
        }
    }
}