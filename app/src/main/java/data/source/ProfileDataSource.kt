package data.source
import android.util.Log
import config.AppMessage
import data.local.AppDataStore
import data.local.AppLocalDatabase
import data.local.dao.UserDao
import data.local.entity.User
import data.result.Result
import domain.HashUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileDataSource @Inject constructor(
    private val db : AppLocalDatabase,
    private val dataStore : AppDataStore,
    private val userDao : UserDao) {

    private var user : User? = null

    fun getUserId() = user?.id

    fun getUserName() = user?.name

    suspend fun checkIfUserRememberAccount() = dataStore.getUserId()

    suspend fun loadUserIfRemember(id : String) {
        withContext(Dispatchers.IO) {
            user = userDao.getUserById(id)
        }
    }

    suspend fun getUiMode() = dataStore.getUiMode()

    suspend fun changeUiMode(mode : Int) = dataStore.changeUiMode(mode)

    suspend fun getLangMode() = dataStore.getLangMode()

    suspend fun changeLanguage(langCode : String) = dataStore.changeLanguage(langCode)

    suspend fun signUp(username : String, password : String) : Result = withContext(Dispatchers.IO) {
        val existed = userDao.validateUserInfo(username, password) == 1
        if(existed) return@withContext Result.Failure(AppMessage.USER_EXISTED)
        val id = HashUseCase().hash(username)
        val newUser = User(id, username, password)
        var error : Result.Error? = null
        db.runInTransaction {
            try {
                userDao.addUser(newUser)
            } catch (e : Exception) {
                error = Result.Error(e.message)
            }
        }
        error?.let {
            return@withContext error!!
        }
        user = newUser
        dataStore.update(id)
        return@withContext Result.Success
    }

    suspend fun signIn(username : String, password : String) = withContext(Dispatchers.IO) {
        try {
            val testUser = userDao.getUserByNameAndPassword(username, password)
            testUser?.let {
                user = it
                dataStore.update(it.id)
                return@withContext Result.Success
            }
        } catch (e : Exception) {
            return@withContext Result.Error(e.message)
        }
        return@withContext Result.Failure(AppMessage.USER_NOT_FOUND)
    }

    suspend fun signOut() = withContext(Dispatchers.IO) {
        try {
            dataStore.update("")
            user = null
            return@withContext Result.Success
        } catch (e : Exception) {
            return@withContext Result.Error(e.message)
        }
    }
}