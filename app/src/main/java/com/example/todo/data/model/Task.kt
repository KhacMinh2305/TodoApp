package com.example.todo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var idTask: Int=0,
    var nameTask: String,
    var contentTask: String,
    var startTime: Long,
    var endTime: Long,
    var priority: String,
) : Serializable


