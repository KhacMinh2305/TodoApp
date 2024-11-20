package ui.viewmodel
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.repo.TaskRepository
import domain.DateTimeUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val taskRepo : TaskRepository
) : ViewModel() {

    private var initialized = false

    private companion object {
        val dateTimeUseCase = DateTimeUseCase()
    }

    fun init() {
        if(initialized) return
        //loadCurrentWeek()
        //loadTaskOnDay(LocalDate.now())
        initialized = true
    }

    private fun loadCurrentWeek() {
        viewModelScope.launch {
            val currWeek = dateTimeUseCase.getWeekDays(LocalDate.now())
            for (date in currWeek) {
                Log.d("Result", "${date.dayOfWeek} : ${date.dayOfMonth}")
            }
        }
    }

    private fun loadTaskOnDay(date : LocalDate) {
        viewModelScope.launch {
            val tasks = taskRepo.getTaskOnDay(date)
        }
    }
}