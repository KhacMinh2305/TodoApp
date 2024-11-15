package com.example.todo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todo.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskRoomDatabaseClass : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        @Volatile
        private var INSTANCE: TaskRoomDatabaseClass? = null

        fun getDatabase(context: Context): TaskRoomDatabaseClass {
            return INSTANCE ?: synchronized(this) {
                val instance = INSTANCE ?: buildDataBase(context)
                INSTANCE = instance
                instance
            }
        }

        private fun buildDataBase(context: Context): TaskRoomDatabaseClass {
            return Room.databaseBuilder(
                context.applicationContext,
                TaskRoomDatabaseClass::class.java,
                "task_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
