package ui.viewmodel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import data.repo.ProfileRepository
import data.repo.TaskRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val taskRepository: TaskRepository,
    private val savedStateHandle: SavedStateHandle) : ViewModel() {

}