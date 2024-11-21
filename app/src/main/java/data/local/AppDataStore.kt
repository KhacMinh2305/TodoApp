package data.local
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import config.AppConstant
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataStore @Inject constructor(@ApplicationContext private val context : Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AppConstant.LOCAL_FILE_NAME)
    private val idKey = stringPreferencesKey(AppConstant.USER_ID)

    suspend fun getUserId() : String = withContext(Dispatchers.IO) {
        context.dataStore.data.first()[idKey] ?: ""
    }

    suspend fun update(userId : String) = withContext(Dispatchers.IO) {
        context.dataStore.edit { it[idKey] = userId }
    }
}