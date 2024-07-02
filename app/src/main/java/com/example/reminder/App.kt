package com.example.reminder

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    private fun createNotificationChannel() {

        val notificationManager = NotificationManagerCompat.from(this)

        val channel = NotificationChannelCompat.Builder(
            REMINDER_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_HIGH
        )
        .setName(getString(R.string.reminder))
        .setDescription(getString(R.string.notification_service_enabled))
        .build()

        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val REMINDER_CHANNEL_ID = "REMINDER_CHANNEL_ID"
    }

}