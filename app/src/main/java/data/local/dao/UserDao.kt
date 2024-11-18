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

    @Query("SELECT * FROM user")
    suspend fun getAll() : List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id : String) : User

    @Query("SELECT * FROM user WHERE name = :name")
    suspend fun getUserByName(name : String) : User

    @Insert(entity = User::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user : User)

    @Delete(entity = User::class)
    suspend fun deleteUser(user : User)

    @Update(entity = User::class)
    suspend fun updateUser(user : User) {

    }
}