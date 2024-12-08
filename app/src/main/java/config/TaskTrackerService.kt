package config
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import config.work.PushNotificationWork
import data.local.entity.Task
import domain.DateTimeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit

const val UPDATE_TAG = "update_task"

class TaskTrackerService : Service() {

    private var tasks = mutableListOf<Task>()
    private val binder = TaskTrackerBinder()
    private val workManager by lazy {
        WorkManager.getInstance(this)
    }
    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private var _workState = MutableStateFlow("")
    val workState : StateFlow<String> = _workState

    inner class TaskTrackerBinder : Binder() {
        fun getService() : TaskTrackerService = this@TaskTrackerService
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    fun receiveData(data : List<Task>) {
        if(data.isEmpty()) return
        tasks.addAll(data)
        val requests = constrainWorkToRequest(data)
        if(requests.isNotEmpty()) {
            workManager.enqueue(requests)
            listenWork()
        }
    }

    private fun constrainWorkToRequest(data: List<Task>) = data.map {
        val requestBuilder = OneTimeWorkRequestBuilder<PushNotificationWork>()
        val inputData = Data.Builder().apply {
            putString("notification_content", "${it.name} has been expired at ${it.endTime}")
            putString("task_id", it.id)
        }.build()
        val workId = it.id.toByteArray()
        val timeToNotify = calculateDelayTime(it)
        return@map requestBuilder
            .setInputData(inputData)
            .addTag(UPDATE_TAG)
            .setId(UUID.nameUUIDFromBytes(workId))
            .setInitialDelay(timeToNotify, TimeUnit.SECONDS)
            .build()
    }

    private fun calculateDelayTime(task : Task) : Long {
        val dateTimeUseCase = DateTimeUseCase()
        val now = LocalDateTime.now()
        val end = dateTimeUseCase.combineDateAndTimeFromTask(task.endDate, task.endTime)
        return dateTimeUseCase.calculateTimeDiff(now, end)
    }

    private fun listenWork() {
        scope.launch {
            workManager.getWorkInfosByTagFlow(UPDATE_TAG).collect {
                it.forEach {workInfo ->
                    if(workInfo.id == UUID.nameUUIDFromBytes(tasks.last().id.toByteArray()) && workInfo.state == WorkInfo.State.SUCCEEDED) {
                        _workState.value = workInfo.outputData.getString("result")!!
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        workManager.cancelAllWork()
        scope.cancel()
    }

}
