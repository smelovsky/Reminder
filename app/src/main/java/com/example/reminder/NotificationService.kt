package com.example.reminder

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.GregorianCalendar
import java.util.Timer
import java.util.TimerTask
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.time.format.DateTimeFormatter

class NotificationService : Service() {

    private var timer: Timer? = null

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    private var notificationId: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        var appRequest: String? = intent.getStringExtra("app_request")
        if (appRequest != null) {
            when (appRequest) {
                "start" -> {
                    timer = startTimer() {

                        GlobalScope.launch {
                            val reminderList = mainViewModel.getDbApi().appDao().getReminderListByTime()

                            var showNotification = false
                            var notification = ""

                            reminderList.forEach() {

                                if (it.Time.isNotBlank() && it.isNotified == false) {

                                    // TODO use SQL request instead

                                    val date = LocalDate.parse(it.Date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                    val time = LocalTime.parse(it.Time, DateTimeFormatter.ofPattern("HH:mm"))

                                    val localDateTimeNow = LocalDateTime.now()
                                    val localDateTime = LocalDateTime.of(date, time)

                                    val period = Period.between(localDateTimeNow.toLocalDate(), localDateTime.toLocalDate())
                                    val duration = Duration.between(localDateTimeNow.toLocalTime(), localDateTime.toLocalTime())

                                    val dif = period.days * 24 * 60 + duration.toMinutes()

                                    if (dif < REMINDER_TIME_MIN) {

                                        GlobalScope.launch {
                                            mainViewModel.getDbApi().appDao()
                                                .setNotifiedFlagById(it.Id, true)
                                        }

                                        showNotification = true
                                        if (notification.isNotBlank()) {
                                            notification += ", "
                                        }
                                        notification += "${it.Name} (${it.Time})"
                                    }

                                }
                            }

                            if (showNotification) {
                                sendMessageToActivity(notification)
                                sendMessage(notificationId++, notification)
                            }
                        }
                    }
                }
                "status" -> {
                    val intentStatus = Intent()
                    intentStatus.action = "STATUS"
                    intentStatus.putExtra("notification_server_enabled", true)
                    sendBroadcast(intentStatus)
                }
            }


        }

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("Reminder", "Notification Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId )
            .setOngoing(true)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.feip))
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setColor(Color.BLUE)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentText(getString(R.string.notification_service_enabled))
            .setWhen(GregorianCalendar.getInstance().getTimeInMillis())
            .build()

        startForeground(55, notificationBuilder )


        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }


    override fun onDestroy() {
        super.onDestroy()

        timer?.cancel()
    }

    private fun startTimer(action: () -> Unit) =

        Timer("Notification Timer").apply {
            val newTimerTask = object : TimerTask() {
                override fun run() {
                    action.invoke()
                }
            }
            schedule(newTimerTask, 0L, TIMER_TIME_MS)
        }

    companion object {
        private const val TIMER_TIME_MS = 20_000L
        private const val REMINDER_TIME_MIN = 60
    }

    private fun sendMessageToActivity(notification: String) {

        val intent = Intent()
        intent.action = "REMINDER"
        intent.putExtra("notification", notification)

        sendBroadcast(intent)
    }

    private fun sendMessage(id: Int, message: String) {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(this, "updates")
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.feip))
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setColor(Color.BLUE)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentTitle(getString(R.string.you_have_a_meeting_with))
            .setContentText(message)
            .setWhen(GregorianCalendar.getInstance().getTimeInMillis())
            .setAutoCancel(true)
            .build()

        notificationManager.notify(id, notificationBuilder)
    }


}