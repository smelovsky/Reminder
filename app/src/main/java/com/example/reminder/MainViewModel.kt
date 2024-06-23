package com.example.reminder

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminder.data.database.AppDatabase
import com.example.reminder.data.database.model.ReminderEntity
import com.example.reminder.data.restapi.repository.Repository
import com.example.reminder.permissions.PermissionsApi
import com.example.reminder.permissions.PermissionsViewState
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
    @ApplicationContext val context: Context,
    ) : ViewModel() {

    val exitFromApp = mutableStateOf(false)

    val showNotification = mutableStateOf(false)
    var notification = ""

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
    // DB
    var reminderListEntity by mutableStateOf(listOf<ReminderEntity>())
    var reminderListEntityByTime by mutableStateOf(listOf<ReminderEntity>())

    var reminderId = 0L
    var reminderTitle by mutableStateOf("")
    var reminderDate by mutableStateOf("")
    var reminderTime by mutableStateOf("")

    var userName by mutableStateOf("")
    var userEmail by mutableStateOf("")
    var userPictureLarge by mutableStateOf("")
    var userPictureThumbnail by mutableStateOf("")


    fun getDbApi (): AppDatabase {
        return appDatabase
    }

    fun getReminderList () {
        GlobalScope.launch {
            reminderListEntity = appDatabase.appDao().getReminderList()
        }
    }

    fun reminderUnselectAllItems() {
        GlobalScope.launch {
            appDatabase.appDao().unselectAllReminders()
            reminderListEntity = appDatabase.appDao().getReminderList()
        }
    }

    fun reminderSelectItem(id: Long, isSelected: Boolean) {
        GlobalScope.launch {
            appDatabase.appDao().unselectAllReminders()
            appDatabase.appDao().selectReminderById(Id = id, isSelected = isSelected)
            reminderListEntity = appDatabase.appDao().getReminderList()
        }
    }

    fun insertReminder (
        title: String,
        name: String,
        email: String,
        date: String,
        time: String,
    ) {

        GlobalScope.launch {

            appDatabase.appDao().unselectAllReminders()

            appDatabase.appDao().insertReminder(ReminderEntity(
                Title = title,
                Name = name,
                PictureLarge = userPictureLarge,
                PictureThumbnail = userPictureThumbnail,
                Email = email,
                Date = date,
                Time = time,
                isNotified = false,
                isSelected = true,
                ))
        }
    }

    fun updateReminder (
        id: Long,
        title: String,
        name: String,
        email: String,
        date: String,
        time: String,
        isNotified: Boolean,
        isSelected: Boolean
    ) {

        GlobalScope.launch {
            appDatabase.appDao().updateReminder(ReminderEntity(
                Id = id,
                Title = title,
                Name = name,
                PictureLarge = userPictureLarge,
                PictureThumbnail = userPictureThumbnail,
                Email = email,
                Date = date,
                Time = time,
                isNotified = isNotified,
                isSelected = isSelected,))
        }
    }

    fun reminderDeleteSelectedItems() {

        GlobalScope.launch {
            appDatabase.appDao().deleteSelectedReminders()
            reminderListEntity = appDatabase.appDao().getReminderList()
        }

    }

    fun getRandomUser() {

        val repository = Repository()

        viewModelScope.launch {
            userListEntity = listOf()
            for (i in 1..15) {
                repository.getRandomUser()
            }

        }

    }

}