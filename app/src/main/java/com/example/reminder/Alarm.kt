package com.example.reminder

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Alarm(
    var id: Long = 0,
    //var alarmId: Int = 0, // Used as W/A for Intent ID which send to ALARM_SERVICE
    val name: String,
    val meetingDate: String,
    val meetingTime: String,
    val reminderTime: Long,
) : Parcelable
