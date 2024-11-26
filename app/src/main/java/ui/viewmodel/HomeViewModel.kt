package ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.local.entity.Task
import data.repo.ProfileRepository
import data.repo.TaskRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileRepo: ProfileRepository,
    private val taskRepo: TaskRepository,
    private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private var _usernameState = MutableLiveData<String>()
    val usernameState : LiveData<String> = _usernameState

    fun loadData() {
        _usernameState.value = profileRepo.getUserName()
        // load Task of this week
        viewModelScope.launch {
            val result = taskRepo.getWeekTask(profileRepo.getUserId()!!, LocalDate.now())
            println("Ket qua : ${result.size}")
            result.forEach {
                println(it.key)
                it.value.forEach {
                    task: Task -> print("${task.name}-${task.endDate}\t")
                }
                println("\n")
            }
        }
        // load task gan nhat chua hoan thanh voi thoi gian hien tai
        // load nhung task cua ngay hom nay
    }
}