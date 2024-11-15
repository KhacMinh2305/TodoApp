package com.example.todo.data.repository
import androidx.annotation.WorkerThread
import com.example.todo.data.model.Task
import com.example.todo.data.database.TaskDao

class TaskRepository(private val taskDao: TaskDao) {

    @WorkerThread
    suspend fun insert(task: Task){
        taskDao.addTask(task)
    }
}
