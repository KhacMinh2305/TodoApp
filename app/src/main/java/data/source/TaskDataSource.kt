package data.source
import data.local.AppLocalDatabase
import data.local.dao.TaskDao
import data.local.entity.Task
import data.result.Result
import domain.DateTimeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskDataSource @Inject constructor(
    private val db : AppLocalDatabase,
    private val taskDao : TaskDao) {

    suspend fun getTaskOnDay(startDate : LocalDate) : List<Task> {
        return withContext(Dispatchers.IO) {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val date = startDate.format(formatter)
            return@withContext taskDao.getTaskByStartDate(date)
        }
    }

    suspend fun getWeekTask(userId : String, weekDay : LocalDate) = withContext(Dispatchers.IO) {
        val dates = DateTimeUseCase().getWeekDays(weekDay).map {
            DateTimeUseCase().convertDateInToString(it)
        }
        return@withContext taskDao.getAllTaskInDateRange(userId, dates)
    }

    suspend fun addTask(task: Task) = withContext(Dispatchers.IO) {
        try {
            db.runInTransaction {
                taskDao.addTask(task)
            }
            return@withContext Result.Success
        } catch (e : Exception) {
            return@withContext Result.Error(e.message)
        }
    }


}