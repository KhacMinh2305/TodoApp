package ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.local.entity.Task
import data.repo.TaskRepository
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(private val taskRepo: TaskRepository) : ViewModel() {


    private var task : Task? = null
    private var initialized = AtomicBoolean(false)

    fun loadInit(id : String) {
        if (initialized.get()) return
        viewModelScope.launch {
            task = taskRepo.getTaskById(id)
            initialized.set(true)
        }
    }

    fun updateTask(/*input params*/) {
        
    }
}