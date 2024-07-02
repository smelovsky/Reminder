package com.example.reminder

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminder.data.database.AppDatabase
import com.example.reminder.data.database.dao.AppDao
import com.example.reminder.data.database.model.ReminderEntity
//import com.example.reminder.data.restapi.repository.Repository
import com.example.reminder.data.restapi.repository.RestapiRepositoryApi
import com.example.reminder.permissions.PermissionsApi
import com.example.reminder.permissions.PermissionsViewState
import com.example.reminder.util.getRemindertime
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserDetails(
    val id: Int,
    val name: String,
    val email: String,
    val picture_thumbnail: String,
    val picture_large: String,
)


@HiltViewModel
class MainViewModel @Inject constructor(
    private val permissionsApi: PermissionsApi,
    private val appDatabase: AppDatabase,
    private val restapiRepositoryApi: RestapiRepositoryApi,
    @ApplicationContext val context: Context,
    ) : ViewModel() {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Permissions

    val permissionsViewState = mutableStateOf(PermissionsViewState())

    fun getPermissionsApi() : PermissionsApi {
        return permissionsApi
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // RANDOM USER GENERATOR
    var userListEntity by mutableStateOf(listOf<UserDetails>())

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //

    var reminderListEntity by mutableStateOf(listOf<ReminderEntity>())

    var reminderId = 0L
    var reminderTitle by mutableStateOf("")
    var reminderDate by mutableStateOf("")
    var reminderTime by mutableStateOf("")

    var userName by mutableStateOf("")
    var userEmail by mutableStateOf("")
    var userPictureLarge by mutableStateOf("")
    var userPictureThumbnail by mutableStateOf("")

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // DB

    fun getAppDao (): AppDao {
        return appDatabase.appDao()
    }

    fun getReminderList () {
        GlobalScope.launch {
            reminderListEntity = getAppDao().getReminderList()
        }
    }

    fun reminderUnselectAllItems() {
        GlobalScope.launch {
            appDatabase.appDao().unselectAllReminders()
            reminderListEntity = getAppDao().getReminderList()
        }
    }

    fun reminderSelectItem(id: Long, isSelected: Boolean) {
        GlobalScope.launch {
            appDatabase.appDao().unselectAllReminders()
            appDatabase.appDao().selectReminderById(Id = id, isSelected = isSelected)
            reminderListEntity = getAppDao().getReminderList()
        }
    }

    fun insertReminder ( reminderEntity: ReminderEntity) {

        GlobalScope.launch {

            appDatabase.appDao().unselectAllReminders()

            val reminderTime = getRemindertime(reminderEntity.Date, reminderEntity.Time)

            val id = getAppDao().insertReminder(reminderEntity)

            val alarm = Alarm(
                id = id,
                name = reminderEntity.Name,
                reminderTime = reminderTime,
                meetingDate = reminderEntity.Date,
                meetingTime = reminderEntity.Time)

            val newAlarmId = alarm.hashCode()

            reminderEntity.AlarmId = newAlarmId
            reminderEntity.Id = id

            appDatabase.appDao().updateReminder(reminderEntity)

            mainViewModel.scheduleReminder(alarm = alarm, oldAlarmId = 0, newAlarmId = newAlarmId)
        }
    }

    fun updateReminder ( reminderEntity: ReminderEntity ) {

        val reminderTime = getRemindertime( reminderEntity.Date, reminderEntity.Time)

        val alarm = Alarm(
            id = reminderEntity.Id,
            name = reminderEntity.Name,
            reminderTime = reminderTime,
            meetingDate = reminderEntity.Date,
            meetingTime = reminderEntity.Time)

        val oldAlarmId = reminderEntity.AlarmId
        val newAlarmId = alarm.hashCode()

        reminderEntity.AlarmId = newAlarmId

        GlobalScope.launch {
            getAppDao().updateReminder(reminderEntity)

            mainViewModel.scheduleReminder(alarm = alarm, oldAlarmId = oldAlarmId, newAlarmId = newAlarmId)

        }
    }

    fun reminderDeleteSelectedItems() {

        GlobalScope.launch {
            getAppDao().deleteSelectedReminders()
            reminderListEntity = appDatabase.appDao().getReminderList()
        }

    }

    fun getRandomUser() {

        viewModelScope.launch {
            userListEntity = listOf()
            for (i in 1..15) {
                mainViewModel.userListEntity += restapiRepositoryApi.getRandomUser(i)
            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //

    fun scheduleReminder(alarm: Alarm, oldAlarmId: Int, newAlarmId: Int) {
        viewModelScope.launch {

            val service = ReminderNotificationService(context)

            service.scheduleNotification(
                alarm = alarm,
                oldAlarmId = oldAlarmId,
                newAlarmId = newAlarmId,
            )
        }
    }

}