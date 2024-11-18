package data.source
import data.local.dao.UserDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileDataSource @Inject constructor(private val userDao : UserDao) {

}