package data.local.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import data.local.entity.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    suspend fun getAll() : List<Task>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTaskById(id : String) : Task?

    @Query("SELECT * FROM task WHERE name = :name")
    suspend fun getTaskByName(name : String) : Task

    @Query("SELECT * FROM task WHERE start_date = :date")
    suspend fun getTaskByStartDate(date : Long) : List<Task>

    @Query("SELECT * FROM task WHERE state = :state")
    suspend fun getFinishedTask(state : Int) : List<Task>

    @Query("WITH RECURSIVE date_range(ele) AS (VALUES(:firstDate) UNION ALL SELECT ele + (:step) FROM date_range LIMIT (:totalDay)) " +
            "SELECT date_range.ele AS date, T2.* from date_range, task as T2 WHERE T2.user_id = (:userId) " +
            "AND T2.start_date <= date AND T2.end_date >= date AND T2.finished_at = -1 " +
            "UNION " +
            "SELECT DISTINCT finished_at as date, * from task " +
            "WHERE user_id = :userId AND finished_at IN (:dates)")
    suspend fun getTasksInDateRange(userId : String, dates : List<Long>, firstDate : Long, totalDay : Int, step : Long) :
            Map<@MapColumn(columnName = "date") Long, List<Task>>?

    @Query("SELECT (:date) AS target, task.* FROM task WHERE task.user_id = (:userId) " +
            "AND task.start_date <= target AND task.end_date >= target AND task.finished_at = -1 " +
            "UNION " +
            "SELECT DISTINCT finished_at as target, * from task " +
            "WHERE user_id = :userId AND finished_at = (:date)")
    suspend fun getTasksAtDate(userId : String, date : Long) : List<Task>?

    @Update(entity = Task::class)
    fun updateTask(task : Task)

    @Insert(entity = Task::class, onConflict = OnConflictStrategy.ABORT)
    fun addTask(task : Task)

}
