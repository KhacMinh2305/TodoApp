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

    // Task 1 : Khai bao cac State Observable de bind du lieu (Khong bind du lieu , chi can khai bao !)
    // (gom nhung states cho nhung truong hien thi du lieu)

    fun loadInit(id : String) {
        if (initialized.get()) return
        viewModelScope.launch {
            task = taskRepo.getTaskById(id)
            initialized.set(true)
        }
    }


    // Task 2 : Check input cho cac ham cap nhat (khong can viet logic cap nhat , chi can kiem tra input nguoi dung co hop le hay khong)
    // Note : Ham updateTask nay goi khi nguoi dung chinh sua thong tin o trong bottom Sheet va bam vao button xac nhan
    fun updateTask(/*input params*/) {
        // Kiem tra tinh hop le cua du lieu (khong can viet logic cap nhat)
    }
}