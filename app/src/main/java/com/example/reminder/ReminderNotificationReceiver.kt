package com.example.reminder

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.reminder.App.Companion.REMINDER_CHANNEL_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReminderNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {

            intent?.getParcelableExtra<Alarm>(ALARM_DATA)?.let { reminder ->
                showNotification(it, reminder)
            }
        }
    }

    private fun showNotification(context: Context, alarm: Alarm) {

        val notificationManager = NotificationManagerCompat.from(context)

        val reminderIntentOpen = Intent(context, ShowActivity::class.java).apply { action = "reminder_id:${alarm.id}" }
        reminderIntentOpen.putExtra("reminder_id", alarm.id)

        val reminderPendingIntentOpen = PendingIntent.getActivity(context, 2, reminderIntentOpen,
            //PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            PendingIntent.FLAG_MUTABLE )

        val notificationBuilder = NotificationCompat.Builder(
            context,
            REMINDER_CHANNEL_ID,
        )
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(context.getString(R.string.you_have_a_meeting_with))
            .setContentText("${alarm.name}, ${alarm.meetingDate}, ${alarm.meetingTime}")
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.feip))
            .setAutoCancel(true)
            .setContentIntent(reminderPendingIntentOpen)


        val notification = notificationBuilder.build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(alarm.id.toInt(), notification)
        }

    }

}