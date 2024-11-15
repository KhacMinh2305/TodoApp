package com.example.todo.data.database

import androidx.room.*
import com.example.todo.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    @Query("SELECT * FROM task")
    fun getTask(): Flow<List<Task>>

    @Update
    fun update(task: Task)

    // Delete a task
    @Delete
    fun delete(task: Task)


}
