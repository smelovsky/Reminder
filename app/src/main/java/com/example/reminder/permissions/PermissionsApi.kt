package com.example.reminder.permissions

import android.app.Activity

interface PermissionsApi {

    fun hasAllPermissions(activity: Activity): Boolean

    fun hasBasePermissions(activity: Activity): Boolean
    fun hasPostNotificationPermissions(activity: Activity): Boolean
    fun hasAlarmPermissions(activity: Activity): Boolean

    fun requestBasePermissions(activity: Activity)
    fun requestPostNotificationPermissions(activity: Activity)
    fun requestAlarmPermissions(activity: Activity)
}
