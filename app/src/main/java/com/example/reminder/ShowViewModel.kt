package com.example.reminder

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.reminder.data.database.AppDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(
    private val appDatabase: AppDatabase,
) : ViewModel() {


    var reminderId by mutableStateOf(-1L)
    var reminderTitle by mutableStateOf("")
    var reminderDate by mutableStateOf("")
    var reminderTime by mutableStateOf("")

    var userName by mutableStateOf("")
    var userEmail by mutableStateOf("")
    var userPictureLarge by mutableStateOf("")
    var userPictureThumbnail by mutableStateOf("")

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // DB

    fun getDbApi (): AppDatabase {
        return appDatabase
    }

    fun getReminderById() {
        GlobalScope.launch {

            val reminder = appDatabase.appDao().getReminderById(reminderId)

            if (reminder == null) {

                reminderId = -1L

                reminderTitle = ""
                reminderDate = ""
                reminderTime = ""

                userName = ""
                userEmail = ""
                userPictureLarge = ""
                userPictureThumbnail = ""
            } else {
                reminderTitle = reminder.Title
                reminderDate = reminder.Date
                reminderTime = reminder.Time

                userName = reminder.Name
                userEmail = reminder.Email
                userPictureLarge = reminder.PictureLarge
                userPictureThumbnail = reminder.PictureThumbnail
            }
                    }
    }

}