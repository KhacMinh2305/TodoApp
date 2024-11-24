package data.local
import androidx.room.Database
import androidx.room.RoomDatabase
import data.local.dao.CommentDao
import data.local.dao.TaskDao
import data.local.dao.UserDao
import data.local.entity.Comment
import data.local.entity.Task
import data.local.entity.User

@Database(entities = [User::class, Task::class, Comment::class], version = 2)
abstract class AppLocalDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun taskDao() : TaskDao
    abstract fun commentDao() : CommentDao
}