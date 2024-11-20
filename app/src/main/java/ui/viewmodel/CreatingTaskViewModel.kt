package ui.viewmodel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import data.repo.ProfileRepository
import data.repo.TaskRepository
import javax.inject.Inject

@HiltViewModel
class CreatingTaskViewModel @Inject constructor(
    private val profileRepo: ProfileRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

}