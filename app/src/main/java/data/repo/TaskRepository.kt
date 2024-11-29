package data.repo
import data.local.entity.Task
import data.source.TaskDataSource
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private var taskDataSource: TaskDataSource) {

    suspend fun getTaskById(id : String) = taskDataSource.getTaskById(id)

    suspend fun getWeekTask(userId : String, weekDay : LocalDate) = taskDataSource.getWeekTask(userId, weekDay)

    suspend fun addTask(task : Task) = taskDataSource.addTask(task)

    suspend fun getTaskByDate(userId : String, date : Long) = taskDataSource.getTaskByDate(userId, date)

    suspend fun getOnGoingTaskAtDate(userId : String, date : Long) = taskDataSource.getOnGoingTaskAtDate(userId, date)

    suspend fun finishTask(taskId: String) = taskDataSource.finishTask(taskId)

    fun clearCacheDataOnSignOut() = taskDataSource.clearCacheDataOnSignOut()
}