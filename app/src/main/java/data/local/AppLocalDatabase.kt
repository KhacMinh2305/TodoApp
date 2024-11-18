package data.local
import androidx.room.Database
import androidx.room.RoomDatabase
import data.local.dao.TaskDao
import data.local.dao.UserDao
import data.local.entity.Task
import data.local.entity.User

@Database(entities = [User::class, Task::class], version = 1)
abstract class AppLocalDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun taskDao() : TaskDao
}