import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todo.data.model.Task
import com.example.todo.data.database.TaskRoomDatabaseClass
import com.example.todo.data.database.TaskDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val taskDao: TaskDao = TaskRoomDatabaseClass.getDatabase(application).taskDao()

    val allTasks: Flow<List<Task>> = taskDao.getTask()

    fun insertTask(task: Task) {
        viewModelScope.launch {
            taskDao.addTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.delete(task)
        }
    }
}
