package com.example.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

const val ALARM_DATA = "alarm_data"

class ReminderNotificationService(
    private val context: Context
) {
    fun scheduleNotification(alarm: Alarm, oldAlarmId: Int, newAlarmId: Int) {
        val intent = Intent(context, ReminderNotificationReceiver::class.java)
        intent.putExtra(ALARM_DATA, alarm)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            newAlarmId,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val oldPendingIntent = PendingIntent.getBroadcast(
            context,
            oldAlarmId,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val alarmService = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = alarm.reminderTime

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (oldAlarmId != 0) {
                    alarmService.cancel(oldPendingIntent)
                }

                alarmService.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
            } catch (exception: SecurityException) {

            }
        }

    }
}