package ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.local.entity.Task
import data.model.WeekDayItem
import data.repo.ProfileRepository
import data.repo.TaskRepository
import domain.DateTimeUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val profileRepo : ProfileRepository,
    private val taskRepo : TaskRepository
) : ViewModel() {

    private var _taskState = MutableLiveData<List<Task>>()
    val taskState : LiveData<List<Task>> = _taskState

    private var _weekDaysState = MutableLiveData<List<WeekDayItem>>()
    val weekDaysState : LiveData<List<WeekDayItem>> = _weekDaysState

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
                DateTimeUseCase().convertDateIntoLong(date))?.toList()
        }
    }
}