package data.repo

import data.local.entity.Task
import data.source.TaskDataSource
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private var taskDataSource: TaskDataSource) {

    suspend fun getTaskOnDay(startDate : LocalDate) : List<Task> {
        return taskDataSource.getTaskOnDay(startDate)
    }

    suspend fun getWeekTask(userId : String, weekDay : LocalDate) = taskDataSource.getWeekTask(userId, weekDay)

    suspend fun addTask(task : Task) = taskDataSource.addTask(task)
}