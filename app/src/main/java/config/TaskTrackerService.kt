package config
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import config.work.PushNotificationWork
import data.local.entity.Task
import domain.DateTimeUseCase
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit

class TaskTrackerService : Service() {

    private var tasks : MutableList<Task>? = null
    private val binder = TaskTrackerBinder()
    private val workManager = WorkManager.getInstance(this)

    inner class TaskTrackerBinder : Binder() {
        fun getService() : TaskTrackerService = this@TaskTrackerService
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    fun receiveData(data : List<Task>) {
        if(tasks != null) return
        tasks = data.toMutableList()
        val requests = constrainWorkToRequest(data)
        if(requests.isNotEmpty()) {
            workManager.enqueue(requests)
        }
    }

    private fun constrainWorkToRequest(data: List<Task>) = data.map {
        val requestBuilder = OneTimeWorkRequestBuilder<PushNotificationWork>()
        val inputData = Data.Builder().apply {
            putString("notification_content", "${it.name} has been expired at ${it.endTime}")
        }.build()
        val timeToNotify = calculateDelayTime(it)
        return@map requestBuilder
            .setInputData(inputData)
            .setId(UUID.nameUUIDFromBytes(it.id.toByteArray()))
            .setInitialDelay(timeToNotify, TimeUnit.SECONDS)
            .build()
    }

    private fun calculateDelayTime(task : Task) : Long {
        val dateTimeUseCase = DateTimeUseCase()
        val now = LocalDateTime.now()
        val end = dateTimeUseCase.combineDateAndTimeFromTask(task.endDate, task.endTime)
        return dateTimeUseCase.calculateTimeDiff(now, end)
    }

    private fun updateTaskOnChange() {

    }

}