package com.example.reminder.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reminder.data.database.dao.AppDao
import com.example.reminder.data.database.model.ReminderEntity

@Database(
    version = 1,
    entities = [
        ReminderEntity::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

}