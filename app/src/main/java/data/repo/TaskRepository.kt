package data.repo

import data.source.TaskDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private var taskDataSource: TaskDataSource) {

}