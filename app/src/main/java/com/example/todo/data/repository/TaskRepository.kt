package com.example.todo.data.repository

import com.example.todo.data.model.Task
import com.example.todo.data.database.TaskDao

class TaskRepository(private val taskDao: TaskDao) {

    // Insert a new task
    suspend fun addTask(task: Task) {
        taskDao.insert(task)
    }

    // Update an existing task
    suspend fun updateTask(task: Task) {
        taskDao.update(task)
    }

    // Delete a task
    suspend fun deleteTask(task: Task) {
        taskDao.delete(task)
    }

    // Get all tasks
    suspend fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks()
    }
}
