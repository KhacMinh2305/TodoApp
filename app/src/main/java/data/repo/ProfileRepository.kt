package data.repo
import data.source.ProfileDataSource
import javax.inject.Singleton
import javax.inject.Inject

@Singleton
class ProfileRepository @Inject constructor(private val profileDataSource: ProfileDataSource) {

}