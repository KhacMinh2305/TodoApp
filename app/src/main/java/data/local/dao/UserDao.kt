package data.local.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import data.local.entity.User

@Dao
interface UserDao {

    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE name = :name AND password = :password) as user_exists")
    suspend fun validateUserInfo(name : String, password : String) : Int

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id : String) : User

    @Query("SELECT * FROM user WHERE name = :name AND password = :password")
    suspend fun getUserByNameAndPassword(name : String, password : String) : User?

    @Insert(entity = User::class, onConflict = OnConflictStrategy.ABORT)
    fun addUser(user : User)

    @Delete(entity = User::class)
    fun deleteUser(user : User)

    @Update(entity = User::class)
    suspend fun updateUser(user : User)
}