package data.repo
import data.local.entity.User
import data.source.ProfileDataSource
import javax.inject.Singleton
import javax.inject.Inject

@Singleton
class ProfileRepository @Inject constructor(private val profileDataSource: ProfileDataSource) {

    fun getUserId() = profileDataSource.getUserId()

    fun getUserName() = profileDataSource.getUserName()

    suspend fun loadIfUserRememberAccount(id : String) {
        profileDataSource.loadIfUserRememberAccount(id)
    }

    suspend fun validateUserInfo(name : String, password : String) : Int {
        return profileDataSource.validateUserInfo(name, password)
    }

    suspend fun getUserByNameAndPassword(name : String, password : String) : User {
        return profileDataSource.getUserByNameAndPassword(name, password)
    }

    suspend fun addUser(user : User) {
        profileDataSource.addUser(user)
    }
}