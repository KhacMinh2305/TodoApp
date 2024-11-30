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
import domain.DateTimeUseCase
import domain.HashUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CreatingTaskViewModel @Inject constructor(
    private val profileRepo: ProfileRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private var _addingTaskState = MutableLiveData<Boolean>()
    val addingTaskState : LiveData<Boolean> = _addingTaskState

    private var _messageState = MutableLiveData<String>()
    val messageState : LiveData<String> = _messageState

    private suspend fun createTaskFromInput(taskName : String, startDate : String,
                                    endDate : String, beginTime : String,
                                    endTime : String, description : String,
                                    priority : Int) : Task {
        val dateTimeUseCase = DateTimeUseCase()
        val today = LocalDate.now()
        val createdAt = "${today.dayOfMonth}/${today.monthValue}/${today.year}"
        val id = HashUseCase().hash(System.currentTimeMillis().toString())
        val createdDay = dateTimeUseCase.convertDateStringIntoLong(createdAt)
        val startOn = dateTimeUseCase.convertDateStringIntoLong(startDate)
        val endOn = dateTimeUseCase.convertDateStringIntoLong(endDate)
        return Task(id, taskName, priority,
            createdDay, startOn, endOn, -1L, beginTime,
            endTime, description, AppConstant.TASK_STATE_NOT_FINISHED, profileRepo.getUserId()!!)
    }

    fun addTask(taskName : String, startDate : String,
                        endDate : String, beginTime : String,
                        endTime : String, description : String,
                        priority : Int) {
        viewModelScope.launch {
            profileRepo.getUserId()?.let {
                val task = createTaskFromInput(taskName, startDate, endDate,
                    beginTime, endTime, description, priority)
                when(taskRepository.addTask(task)) {
                    is Result.Success -> {
                        _messageState.value = AppMessage.RESULT_ADD_TASK_SUCCESS
                        _addingTaskState.value = true
                    }
                    is Result.Error -> {
                        _messageState.value = AppMessage.RESULT_ADD_TASK_FAILED
                    }
                    else -> _messageState.value = AppMessage.UNDEFINED_ERROR
                }
            }
        }
    }
}