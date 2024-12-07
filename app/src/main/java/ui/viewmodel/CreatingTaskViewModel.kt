package ui.viewmodel
import android.util.Range
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import env_variable.AppConstant
import env_variable.AppMessage
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

    private var _addingTaskState = MutableLiveData<LocalDate>()
    val addingTaskState : LiveData<LocalDate> = _addingTaskState

    private var _updateCalenderState = MutableLiveData<Range<LocalDate>>()
    val updateCalenderState : LiveData<Range<LocalDate>> = _updateCalenderState

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
        val isValid = validate(taskName, startDate, endDate, beginTime, endTime, description) && validatePriority(priority)
        if(!isValid) return
        viewModelScope.launch {
            profileRepo.getUserId()?.let {
                val task = createTaskFromInput(taskName, startDate, endDate,
                    beginTime, endTime, description, priority)
                when(taskRepository.addTask(task)) {
                    is Result.Success -> {
                        _messageState.value = AppMessage.RESULT_ADD_TASK_SUCCESS
                        _addingTaskState.value = LocalDate.now()
                        emitReloadCalenderDataState(startDate, endDate)
                    }
                    is Result.Error -> {
                        _messageState.value = AppMessage.RESULT_ADD_TASK_FAILED
                    }
                    else -> _messageState.value = AppMessage.UNDEFINED_ERROR
                }
            }
        }
    }

    private fun emitReloadCalenderDataState(startDate : String, endDate : String) {
        val dateTimeUseCase = DateTimeUseCase()
        _updateCalenderState.value = dateTimeUseCase.getDateRangeFromStrings(startDate, endDate)
    }

    private fun validateEmptyInput(input: String): Boolean {
        if (input.isEmpty()) {
            _messageState.value = AppMessage.EMPTY_INPUT
            return false
        }
        return true
    }

    private fun validatePriority(priority : Int) : Boolean {
        if(priority == 0) {
            _messageState.value = AppMessage.EMPTY_INPUT
            return false
        }
        return true
    }

    private fun validate(vararg inputs : String) : Boolean {
        inputs.forEach {
            if(!validateEmptyInput(it)) return false
        }
        return true
    }
}