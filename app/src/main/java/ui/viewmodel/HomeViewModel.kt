package ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import config.AppConstant
import config.AppMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import data.local.entity.Task
import data.model.WeekProgressItem
import data.repo.ProfileRepository
import data.repo.TaskRepository
import data.result.Result
import domain.DateTimeUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileRepo: ProfileRepository,
    private val taskRepo: TaskRepository) : ViewModel() {

    private lateinit var onGoingListTask : MutableList<Task>

    private var _usernameState = MutableLiveData<String>()
    val usernameState : LiveData<String> = _usernameState

    private var _weekTotalTaskStates = MutableLiveData<Int>()
    val weekTotalTaskState : LiveData<Int> = _weekTotalTaskStates

    private var _weekProgressState = MutableLiveData<List<WeekProgressItem>>()
    val weekProgressState : LiveData<List<WeekProgressItem>> = _weekProgressState

    private var _todayTaskState = MutableLiveData<List<Task>>()
    val todayTaskState : LiveData<List<Task>> = _todayTaskState

    private var _onGoingTaskNameState = MutableLiveData<String>()
    val onGoingTaskNameState : LiveData<String> = _onGoingTaskNameState

    private var _onGoingTaskTimeState = MutableLiveData<String>()
    val onGoingTaskState : LiveData<String> = _onGoingTaskTimeState

    private var _onGoingTaskDescriptionState = MutableLiveData<String>()
    val onGoingTaskDescriptionState : LiveData<String> = _onGoingTaskDescriptionState

    private var _onGoingTaskPriorityState = MutableLiveData<Int>()
    val onGoingTaskPriorityState : LiveData<Int> = _onGoingTaskPriorityState

    private var _notFinishedTask = MutableLiveData<Int>()
    val notFinishedTask : LiveData<Int> = _notFinishedTask

    private var _progressState = MutableLiveData<Int>()
    val progressState : LiveData<Int> = _progressState

    private var _finishedTaskState = MutableLiveData(false)
    val finishedTaskState : LiveData<Boolean> = _finishedTaskState

    private var _messageState = MutableLiveData<String>()
    val messageState : LiveData<String> = _messageState

    fun loadData() {
        loadUserName()
        viewModelScope.launch {
            loadWeekProgress()
            loadTodayTasks()
            loadOnGoingTaskToday()
            emitOnGoingStates()
        }
    }

    private fun loadUserName() {
        _usernameState.value = profileRepo.getUserName()
    }

    private suspend fun loadWeekProgress() {
        val result = taskRepo.getWeekTask(profileRepo.getUserId()!!, LocalDate.now())
        val weekProgress = calculateWeekProgress(result)
        _weekProgressState.value = weekProgress
    }

    private fun calculateWeekProgress(result : Map<Long, List<Task>>) : List<WeekProgressItem> {
        val weekItem = mutableListOf<WeekProgressItem>()
        var totalTask = 0
        result.forEach {
            val dayOfWeek = DateTimeUseCase().convertLongDayOfWeek(it.key)
            val progress = calculateDayProgress(it.value)
            weekItem.add(WeekProgressItem(dayOfWeek, progress))
            totalTask += it.value.size
        }
        _weekTotalTaskStates.value = totalTask
        return weekItem.toList()
    }

    private fun calculateDayProgress(tasks : List<Task>) : Int {
        var finished = 0
        if(tasks.isEmpty()) return 0
        tasks.forEach {
            if(it.state == AppConstant.TASK_STATE_FINISHED) finished++
        }
        return (finished * 100 / tasks.size)
    }

    private suspend fun loadTodayTasks() {
        val today = DateTimeUseCase().convertDateIntoLong(LocalDate.now())
        val tasks = taskRepo.getTaskByDate(profileRepo.getUserId()!!, today)
        _todayTaskState.value = tasks?.toList() ?: emptyList()
    }

    private suspend fun loadOnGoingTaskToday() {
        val today = DateTimeUseCase().convertDateIntoLong(LocalDate.now())
        val tasks = taskRepo.getOnGoingTaskAtDate(profileRepo.getUserId()!!, today)
        onGoingListTask = tasks
    }

    private fun emitOnGoingStates() {
        val hasNoTaskToDo = onGoingListTask.isEmpty()
        _finishedTaskState.value = hasNoTaskToDo
        if(hasNoTaskToDo) return
        val onGoingTask = onGoingListTask.last()
        _onGoingTaskNameState.value = onGoingTask.name
        _onGoingTaskTimeState.value = "${onGoingTask.startTime} - ${onGoingTask.endTime}"
        _onGoingTaskDescriptionState.value = onGoingTask.description
        _onGoingTaskPriorityState.value = onGoingTask.priority
        var count = 0
        _todayTaskState.value?.forEach {
            count = if(it.state == AppConstant.TASK_STATE_FINISHED) count + 1 else count
        }
        _progressState.value = if(count != 0) count * 100 / _todayTaskState.value!!.size else 0
        _notFinishedTask.value = onGoingListTask.size
    }

    private suspend fun reloadOnFinishTask() {
        loadWeekProgress()
        loadTodayTasks()
        if(onGoingListTask.isEmpty()) {
            _finishedTaskState.value = true
            return
        }
        onGoingListTask.removeAt(onGoingListTask.size - 1)
        emitOnGoingStates()
        //TODO : Notify AppViewModel to update data in Calendar (if must) and Profile task
    }

    fun finishTask() {
        if(onGoingListTask.isEmpty()) return
        val taskId = onGoingListTask.last().id
        viewModelScope.launch {
            when(val result = taskRepo.finishTask(taskId)) {
                is Result.Success -> reloadOnFinishTask()
                is Result.Error -> result.message?.let { _messageState.value = it }
                else -> _messageState.value = AppMessage.UNDEFINED_ERROR
            }
        }
    }
}