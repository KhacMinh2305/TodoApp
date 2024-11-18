package data.local.dao
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import data.local.entity.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    suspend fun getAll() : List<Task>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTaskById(id : String) : Task

    @Query("SELECT * FROM task WHERE name = :name")
    suspend fun getTaskByName(name : String) : Task

    @Query("SELECT * FROM task WHERE start_date = :date")
    suspend fun getTaskByDate(date : String) : List<Task>

    @Query("SELECT * FROM task WHERE state = :state")
    suspend fun getFinishedTask(state : Int) : List<Task>

    @Update(entity = Task::class)
    suspend fun updateTask(task : Task)

    // more queries here ... (only declare when using)
}