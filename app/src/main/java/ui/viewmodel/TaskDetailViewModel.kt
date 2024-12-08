package ui.viewmodel
import android.util.Range
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.local.entity.Task
import data.repo.TaskRepository
import data.result.Result
import domain.DateTimeUseCase
import env_variable.AppMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(private val taskRepo: TaskRepository) : ViewModel() {

    private var task : Task? = null
    private var initialized = false

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _priority = MutableLiveData<Int>()
    val priority: LiveData<Int> = _priority
    private val _startDate = MutableLiveData<String>()
    val startDate: LiveData<String> = _startDate

    private val _endDate = MutableLiveData<String>()
    val endDate: LiveData<String> = _endDate

    private val _startTime = MutableLiveData<String>()
    val startTime: LiveData<String> = _startTime

    private val _endTime = MutableLiveData<String>()
    val endTime: LiveData<String> = _endTime

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private var  _updateTaskState = MutableSharedFlow<Range<LocalDate>>()
    val updateTaskState = _updateTaskState.asSharedFlow()

    private var _messageState = MutableLiveData<String>()
    val messageState : LiveData<String> = _messageState

    fun loadInit(id : String) {
        if (initialized) return
        viewModelScope.launch {
            task = taskRepo.getTaskById(id)
            task?.let { bindDataToView(it) }
            println(task?.name)
            initialized = true
        }
    }

    private fun bindDataToView(result : Task) {
        val dateTimeUseCase = DateTimeUseCase()
        _name.value = result.name
        _priority.value = result.priority
        _startDate.value = dateTimeUseCase.convertLongToDateString(result.startDate)
        _endDate.value = dateTimeUseCase.convertLongToDateString(result.endDate)
        _startTime.value = result.startTime
        _endTime.value = result.endTime
        _description.value = result.description
    }

    private fun validate(vararg inputs : String) : Boolean {
        inputs.forEach {
            if(it.isNotEmpty()) return true
        }
        _messageState.value = AppMessage.EMPTY_INPUT
        return false
    }

    fun updateTask(taskName : String, startDate : String, endDate : String,
                   beginTime : String, endTime : String, description : String) {
        val isValid = validate(taskName, startDate, endDate, beginTime, endTime, description)
        if(task == null) return
        if(!isValid) return
        val dateTimeUseCase = DateTimeUseCase()
        val startAtDate = if(startDate.isNotEmpty()) dateTimeUseCase.convertDateStringIntoLong(startDate) else task?.startDate!!
        val endAtDate = if(endDate.isNotEmpty()) dateTimeUseCase.convertDateStringIntoLong(endDate) else task?.endDate!!
        val updatedTask = task?.copy(
            name = taskName.ifEmpty { task?.name!! },
            startDate = startAtDate,
            endDate = endAtDate,
            startTime = beginTime.ifEmpty { task?.startTime!! },
            endTime = endTime.ifEmpty { task?.endTime!! },
            description = description.ifEmpty { task?.description!!})
        task = updatedTask
        viewModelScope.launch {
            when (taskRepo.updateTask(task!!)) {
                is Result.Success -> {
                    val dateRange = dateTimeUseCase.getDateRangeFromLong(task?.startDate!!, task?.endDate!!)
                    _updateTaskState.emit(dateRange)
                }
                is Result.Error -> _messageState.value = AppMessage.UPDATE_TASK_FAILED
                else -> _messageState.value = AppMessage.UNDEFINED_ERROR
            }
        }
    }
}