package ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import config.AppConstant
import config.AppMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import data.local.entity.Task
import data.model.WeekDayItem
import data.repo.ProfileRepository
import data.repo.TaskRepository
import data.result.Result
import domain.DateTimeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val profileRepo : ProfileRepository,
    private val taskRepo : TaskRepository
) : ViewModel() {

    var _currentSelectedDateIndex = 0
    var _currentSelectedDate = LocalDate.now()

    private var _taskState = MutableStateFlow<List<Task>>(emptyList())
    val taskState : StateFlow<List<Task>> = _taskState

    private var _weekDaysState = MutableStateFlow<List<WeekDayItem>>(emptyList())
    val weekDaysState : StateFlow<List<WeekDayItem>> = _weekDaysState

    private var _deleteTaskState = MutableLiveData<Boolean>()
    val deleteTaskState : LiveData<Boolean> = _deleteTaskState

    private var _messageState = MutableLiveData<String>()
    val messageState : LiveData<String> = _messageState

    init {
        loadWeekDays(LocalDate.now())
        loadTasks(LocalDate.now())
    }

    fun loadWeekDays(dateInWeek : LocalDate) {
        viewModelScope.launch {
            val weekDays = DateTimeUseCase().getWeekDays(dateInWeek).toMutableList()
            val weekDayItems = weekDays.map {
                WeekDayItem(it.dayOfWeek.name.substring(0, 3), it.dayOfMonth, it.monthValue, it.year)
            }.toMutableList()
            _weekDaysState.value = weekDayItems
        }
    }

    fun loadTasks(date: LocalDate) {
        viewModelScope.launch {
            _taskState.value = taskRepo.getTaskByDate(profileRepo.getUserId()!!,
                DateTimeUseCase().convertDateIntoLong(date))?.toList() ?: emptyList()
        }
    }

    fun deleteTask(task : Task) {
        if(task.state != AppConstant.TASK_STATE_NOT_FINISHED) {
            _messageState.value = AppMessage.NOT_ALLOW_DELETE_TASK
            return
        }
        viewModelScope.launch {
            when(taskRepo.deleteTask(task.id)) {
                is Result.Success -> {
                    validateRecyclerViewOnDeletingTask(task)
                    reloadHomeDataOnDeletingTask(task)
                }
                is Result.Error -> {}
                else -> {}
            }
        }
    }

    private fun validateRecyclerViewOnDeletingTask(task : Task) {
        val validatedData = _taskState.value
        val deletedIndex = validatedData.indexOf(task)
        if(deletedIndex >= 0) {
            _taskState.value = validatedData.toMutableList().apply {
                removeAt(deletedIndex)
            }
        }
    }

    private fun reloadHomeDataOnDeletingTask(task: Task) {
        val today = DateTimeUseCase().convertDateIntoLong(LocalDate.now())
        val mustReload = task.startDate <= today && today <= task.endDate
        if(mustReload) {
            _deleteTaskState.value = true
        }
    }
}