package com.example.todo.data.database

import androidx.room.*
import com.example.todo.data.model.Task

@Dao
interface TaskDao {

    // Insert new task
    @Insert
    suspend fun insert(task: Task)

    // Update an existing task
    @Update
    suspend fun update(task: Task)

    // Delete a task
    @Delete
    suspend fun delete(task: Task)

    // Get all tasks
    @Query("SELECT * FROM task")
    suspend fun getAllTasks(): List<Task>
}
