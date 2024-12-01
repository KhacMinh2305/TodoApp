package ui.viewmodel

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import config.AppConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import data.local.entity.Task
import data.repo.TaskRepository
import domain.DateTimeUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(private val taskRepo: TaskRepository) : ViewModel() {


    private var task: Task? = null
    private var initialized = AtomicBoolean(false)

    // Task 1 : Khai bao cac State Observable de bind du lieu (Khong bind du lieu , chi can khai bao !)
    // (gom nhung states cho nhung truong hien thi du lieu)

    private val _taskNameState = MutableLiveData<String>()
    val taskNameState: LiveData<String> get() = _taskNameState

    private val _startDateState = MutableLiveData<String>()
    val startDateState: LiveData<String> get() = _startDateState

    private val _endDateState = MutableLiveData<String>()
    val endDateState: LiveData<String> get() = _endDateState

    private val _timeState = MutableLiveData<String>()
    val timeState: LiveData<String> get() = _timeState

    private val _priorityState = MutableLiveData<String>()
    val priorityState: LiveData<String> get() = _priorityState

    private val _descriptionState = MutableLiveData<String>()
    val descriptionState: LiveData<String> get() = _descriptionState

    private val _commentState = MutableLiveData<String>()
    val commentState: LiveData<String> get() = _commentState

    fun loadInit(id: String) {
        if (initialized.get()) return
        viewModelScope.launch {

            task = taskRepo.getTaskById(id)

            }
            initialized.set(true)
    }

    fun validateInput(
        context: Context,
        taskName: String,
        startDate: String,
        endDate: String,
        startTime: String,
        endTime: String,
        taskDescription: String
    ): Boolean {
        // Kiểm tra tên công việc
        if (taskName.isBlank()) {
            Toast.makeText(context, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }

        // Kiểm tra ngày bắt đầu
        if (startDate.isBlank()) {
            Toast.makeText(context, "Start date cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidDate(context, startDate)) return false

        // Kiểm tra ngày kết thúc
        if (startDate.isBlank()) {
            Toast.makeText(context, "End date cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidDate(context, endDate)) return false

        // Kiểm tra thời gian bắt đầu
        if (startTime.isBlank()) {
            Toast.makeText(context, "Start time cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidTime(context, startTime, "Invalid start time format")) return false

        // Kiểm tra thời gian kết thúc
        if (endTime.isBlank()) {
            Toast.makeText(context, "End time cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidTime(context, endTime, "Invalid end time format")) return false

        // Kiểm tra ngày và thời gian
        if (!isValidStartEndDateTime(context, startDate, endDate, startTime, endTime)) return false

        // Kiểm tra mô tả công việc
        if (taskDescription.isBlank()) {
            Toast.makeText(context, "Task description cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    //update task

}

    // Task 2 : Check input cho cac ham cap nhat (khong can viet logic cap nhat , chi can kiem tra input nguoi dung co hop le hay khong)
    // Note : Ham updateTask nay goi khi nguoi dung chinh sua thong tin o trong bottom Sheet va bam vao button xac nhan
    // Hàm kết hợp kiểm tra và cập nhật task
// Kiểm tra định dạng ngày
    // Kiểm tra định dạng ngày
    fun isValidDate(context: Context, date: String): Boolean {
        if (date.isBlank() || !tryParseDate(date)) {
            Toast.makeText(context, "Invalid date format", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // Kiểm tra định dạng giờ
    fun isValidTime(context: Context, time: String, errorMessage: String): Boolean {
        if (time.isBlank() || !tryParseTime(time)) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // Kiểm tra nếu thời gian bắt đầu và kết thúc hợp lệ
    fun isValidStartEndDateTime(context: Context, startDate: String, endDate: String, startTime: String, endTime: String): Boolean {
        val startDateObj = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val endDateObj = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        // Kiểm tra ngày kết thúc phải sau ngày bắt đầu
        if (endDateObj.isBefore(startDateObj)) {
            Toast.makeText(context, "End date must be after start date", Toast.LENGTH_SHORT).show()
            return false
        }

        if (startDate == endDate) {
            val startLocalTime = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"))
            val endLocalTime = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"))

            // Kiểm tra giờ kết thúc phải sau giờ bắt đầu
            if (endLocalTime.isBefore(startLocalTime)) {
                Toast.makeText(context, "End time must be after start time", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    // Phương thức kiểm tra nếu ngày hợp lệ
    fun tryParseDate(date: String): Boolean {
        return try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            true
        } catch (e: Exception) {
            false
        }
    }

    // Phương thức kiểm tra nếu giờ hợp lệ
    fun tryParseTime(time: String): Boolean {
        return try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
            true
        } catch (e: Exception) {
            false
        }
    }






