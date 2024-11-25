package ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import config.AppConstant
import config.AppMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import data.local.entity.Task
import data.repo.ProfileRepository
import data.repo.TaskRepository
import data.result.Result
import domain.HashUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CreatingTaskViewModel @Inject constructor(
    private val profileRepo: ProfileRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private var _addTaskState = MutableLiveData<Boolean>()
    val addTaskState : LiveData<Boolean> = _addTaskState

    private var _messageState = MutableLiveData<String>()
    val messageState : LiveData<String> = _messageState

    fun addTask(taskName : String, startDate : String,
                        endDate : String, beginTime : String,
                        endTime : String, description : String,
                        priority : Int) {

        val today = LocalDate.now()
        val createdAt = "${today.dayOfMonth}/${today.monthValue}/${today.year}"
        viewModelScope.launch {
            val id = HashUseCase().hash(taskName.plus(startDate))
            profileRepo.getUserId()?.let {
                val task = Task(id, taskName, priority,
                    createdAt, startDate, endDate, beginTime,
                    endTime, description, AppConstant.TASK_STATE_NOT_FINISHED, it)
                when(taskRepository.addTask(task)) {
                    is Result.Success -> {
                        _messageState.value = AppMessage.RESULT_ADD_TASK_SUCCESS
                        // TODO: notify to other fragment to update
                    }
                    is Result.Error -> _messageState.value = AppMessage.RESULT_ADD_TASK_SUCCESS
                    else -> _messageState.value = AppMessage.UNDEFINED_ERROR
                }
            }
        }
    }
}