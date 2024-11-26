package data.local.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import data.local.entity.Task
import data.local.entity.WeekTask

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    suspend fun getAll() : List<Task>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTaskById(id : String) : Task

    @Query("SELECT * FROM task WHERE name = :name")
    suspend fun getTaskByName(name : String) : Task

    @Query("SELECT * FROM task WHERE start_date = :date")
    suspend fun getTaskByStartDate(date : String) : List<Task>

    @Query("SELECT * FROM task WHERE state = :state")
    suspend fun getFinishedTask(state : Int) : List<Task>

    @Query("SELECT Task1.end_date as endDate, Task2.* FROM task Task1 " +
            "JOIN task Task2 ON Task1.id = Task2.id" +
            " WHERE Task1.user_id = :userId AND endDate IN (:dates)")
    suspend fun getAllTaskInDateRange(userId : String, dates : List<String>) :
            Map<@MapColumn(columnName = "endDate") String, List<Task>>

    @Update(entity = Task::class)
    suspend fun updateTask(task : Task)

    @Insert(entity = Task::class, onConflict = OnConflictStrategy.ABORT)
    fun addTask(task : Task)

    // more queries here ... (only declare when using)
}