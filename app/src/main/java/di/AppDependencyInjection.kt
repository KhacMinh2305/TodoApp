package di
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import data.local.AppLocalDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDependencyInjection {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context : Context) : AppLocalDatabase {
        return Room.databaseBuilder(context, AppLocalDatabase::class.java, "todo_database").build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appLocalDatabase: AppLocalDatabase) = appLocalDatabase.userDao()

    @Provides
    @Singleton
    fun provideTaskDao(appLocalDatabase: AppLocalDatabase) = appLocalDatabase.taskDao()

    @Provides
    @Singleton
    fun provideCommentDao(appLocalDatabase: AppLocalDatabase) = appLocalDatabase.commentDao()

}