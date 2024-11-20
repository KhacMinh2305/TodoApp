package data.source
import data.local.dao.TaskDao
import data.local.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskDataSource @Inject constructor(private val taskDao : TaskDao) {

    suspend fun getTaskOnDay(startDate : LocalDate) : List<Task> {
        return withContext(Dispatchers.IO) {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val date = startDate.format(formatter)
            return@withContext taskDao.getTaskByStartDate(date)
        }
    }


}