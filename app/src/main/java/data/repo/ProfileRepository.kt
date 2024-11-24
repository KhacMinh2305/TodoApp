package data.repo
import data.source.ProfileDataSource
import javax.inject.Singleton
import javax.inject.Inject

@Singleton
class ProfileRepository @Inject constructor(private val profileDataSource: ProfileDataSource) {

    fun getUserId() = profileDataSource.getUserId()

    fun getUserName() = profileDataSource.getUserName()

    suspend fun checkIfUserRememberAccount() = profileDataSource.checkIfUserRememberAccount()

    suspend fun loadUserIfRemember(id : String) = profileDataSource.loadUserIfRemember(id)

    suspend fun signUp(username : String, password : String)  = profileDataSource.signUp(username, password)

    suspend fun signIn(username : String, password : String) = profileDataSource.signIn(username, password)

    suspend fun signOut() = profileDataSource.signOut()

}