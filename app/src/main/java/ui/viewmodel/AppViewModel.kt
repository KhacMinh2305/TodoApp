package ui.viewmodel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import data.repo.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val profileRepo : ProfileRepository)
    : ViewModel() {

}