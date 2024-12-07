package config.work
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service.NOTIFICATION_SERVICE
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todo.R
import env_variable.AppConstant

class PushNotificationWork(val context : Context, private val param : WorkerParameters) : Worker(context, param) {

    private var taskExpiredBuilder = NotificationCompat.Builder(context, AppConstant.TASK_EXPIRED_CHANEL_ID)
        .setSmallIcon(R.drawable.splash_logo)
        .setContentTitle("You missed a task !")
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    override fun doWork(): Result {
        val notificationContent = inputData.getString("notification_content")
        pushNotification(notificationContent!!)
        // notify update data here
        return Result.success()
    }

    private fun pushNotification(taskName: String) {
        registryNotificationChannel()
        val notification = createNotification(taskName)
        with(NotificationManagerCompat.from(context)) {
            val permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            if(permission != PackageManager.PERMISSION_GRANTED) {
                //TODO: Notify for user to grant permission
                println("Chua cap quyen thong bao !")
                return@with
            }
            notify(2301, notification) // sua lai id sau khi fix
        }
    }

    private fun createNotification(taskName : String) = taskExpiredBuilder
        .setContentText("\uD83D\uDE2D Ops! \"$taskName\" has been expired !")
        .build()

    private fun registryNotificationChannel() {
        val chanelName = context.getString(R.string.task_expired_chanel_name)
        val chanelDescription = context.getString(R.string.task_expired_chanel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val chanel = NotificationChannel(AppConstant.TASK_EXPIRED_CHANEL_ID, chanelName, importance).apply {
            description = chanelDescription
        }
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(chanel)
    }
}