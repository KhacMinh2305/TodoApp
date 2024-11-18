package data.source
import data.local.dao.TaskDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskDataSource @Inject constructor(private val taskDao : TaskDao) {

}