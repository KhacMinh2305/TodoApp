package data.source
import data.local.dao.UserDao
import data.local.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileDataSource @Inject constructor(private val userDao : UserDao) {

    private var user : User? = null

    fun getUserId() = user?.id

    fun getUserName() = user?.name

    suspend fun loadIfUserRememberAccount(id : String) {
        withContext(Dispatchers.IO) {
            user = userDao.getUserById(id)
        }
    }

    suspend fun validateUserInfo(name : String, password : String) : Int {
        return withContext(Dispatchers.IO) {
            return@withContext userDao.validateUserInfo(name, password)
        }
    }

    suspend fun getUserByNameAndPassword(name : String, password : String) : User {
        return withContext(Dispatchers.IO) {
            return@withContext userDao.getUserByNameAndPassword(name, password)
        }
    }

    suspend fun addUser(user : User) {
        withContext(Dispatchers.IO) {
            userDao.addUser(user)
        }
    }

}