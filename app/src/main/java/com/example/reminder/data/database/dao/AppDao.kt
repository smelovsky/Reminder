package com.example.reminder.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.reminder.data.database.model.ReminderEntity


@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReminder(reminderEntity: ReminderEntity): Long

    @Update
    suspend fun updateReminder(trackEntity: ReminderEntity)

    @Query(value = "SELECT * FROM reminders")
    fun getReminderList(): List<ReminderEntity>

    @Query(value = "SELECT * FROM reminders WHERE id = :reminderId ")
    fun getReminderById(reminderId: Long): ReminderEntity

    @Query(value = "SELECT * FROM reminders")
    fun getReminderListByTime(): List<ReminderEntity>

    @Query(value = """
        UPDATE reminders SET
        name = :reminderName
        where id = :reminderId
    """)
    suspend fun updateReminder(reminderId: Long, reminderName: String)

    @Query(value = """
            UPDATE reminders SET
            is_notified = :isNotified
            where id = :Id
        """)
    suspend fun setNotifiedFlagById(Id: Long, isNotified: Boolean)

    @Query(value = """
        UPDATE reminders SET
        is_selected = :isSelected
        where id = :Id
    """)
    suspend fun selectReminderById(Id: Long, isSelected: Boolean)

    @Query(value = """
        UPDATE reminders SET
        is_selected = false
    """)
    suspend fun unselectAllReminders()

    @Query(value = """
        DELETE FROM reminders
        where id = :Id
    """)
    suspend fun deleteReminder(Id: Long)

    @Query(value = """
        DELETE FROM reminders
        where is_selected = true
    """)
    suspend fun deleteSelectedReminders()

    @Query(value = """
        DELETE FROM reminders
    """)
    suspend fun deleteAllReminders()

}