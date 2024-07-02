package com.example.reminder.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminders",
)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var Id: Long = 0,

    @ColumnInfo(name = "alarm_id")
    var AlarmId: Int = 0,

    @ColumnInfo(name = "title")
    val Title: String,

    @ColumnInfo(name = "name")
    val Name: String,

    @ColumnInfo(name = "email")
    val Email: String,

    @ColumnInfo(name = "picture_large")
    val PictureLarge: String,

    @ColumnInfo(name = "picture_thumbnail")
    val PictureThumbnail: String,

    @ColumnInfo(name = "date")
    val Date: String,

    @ColumnInfo(name = "time")
    val Time: String,

    @ColumnInfo(name = "is_selected")
    val isSelected: Boolean,

    @ColumnInfo(name = "is_notified")
    val isNotified: Boolean,

    )
