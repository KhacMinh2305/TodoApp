package com.example.todo.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Parcelize
@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var idTask: Int=0,
    var nameTask: String,
    var contentTask: String,
    var startTime: Long,
    var endTime: Long,
    var priority: String,
) : Parcelable


