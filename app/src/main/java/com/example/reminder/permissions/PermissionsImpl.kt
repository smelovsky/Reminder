package com.example.reminder.permissions

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.reminder.mainViewModel


var basePermissions = arrayOf(
    Manifest.permission.INTERNET,
    Manifest.permission.ACCESS_NOTIFICATION_POLICY,
    Manifest.permission.SCHEDULE_EXACT_ALARM,
)

val postNotificationPermissions = arrayOf(
    Manifest.permission.POST_NOTIFICATIONS,
)


data class PermissionsViewState(
    val INTERNET: Boolean = false,
    val POST_NOTIFICATIONS: Boolean = false,
    val ACCESS_NOTIFICATION_POLICY: Boolean = false,
    val SCHEDULE_EXACT_ALARM: Boolean = false,

    val permissionsGranted: Boolean = false,
)

class PermissionsImpl(val context: Context): PermissionsApi {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            basePermissions = arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            )
        }
    }

    override fun hasAllPermissions(activity: Activity): Boolean{

        var result = true

        if (!hasBasePermissions(activity)) {
            result = false
        }

        if (!hasPostNotificationPermissions(activity)) {
            result = false
        }

        if (!hasAlarmPermissions(activity)) {
            result = false
        }

        mainViewModel.permissionsViewState.value =
            mainViewModel.permissionsViewState.value.copy(permissionsGranted = result)

        return result
    }

    override fun hasBasePermissions(activity: Activity): Boolean{
        var result = true
        basePermissions.forEach {

            val permission = ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            if ( !permission)
            {
                result = false
            }
            when (it) {

                Manifest.permission.INTERNET -> mainViewModel.permissionsViewState.value =
                    mainViewModel.permissionsViewState.value.copy(INTERNET = permission)
                Manifest.permission.ACCESS_NOTIFICATION_POLICY -> mainViewModel.permissionsViewState.value =
                    mainViewModel.permissionsViewState.value.copy(ACCESS_NOTIFICATION_POLICY = permission)
                Manifest.permission.SCHEDULE_EXACT_ALARM -> mainViewModel.permissionsViewState.value =
                    mainViewModel.permissionsViewState.value.copy(SCHEDULE_EXACT_ALARM = permission)

            }
        }

        return result
    }

    override fun hasPostNotificationPermissions(activity: Activity): Boolean{

        val permission =

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                        == PackageManager.PERMISSION_GRANTED)
            } else {
                true
            }

        mainViewModel.permissionsViewState.value =
            mainViewModel.permissionsViewState.value.copy(POST_NOTIFICATIONS = permission)

        return permission
    }

    override fun hasAlarmPermissions(activity: Activity): Boolean{

        val permission =

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
            if (alarmManager!!.canScheduleExactAlarms()) {
                true
            } else {
                false
            }
        } else {
            (ContextCompat.checkSelfPermission(context, Manifest.permission.SCHEDULE_EXACT_ALARM)
                    == PackageManager.PERMISSION_GRANTED)
        }

        mainViewModel.permissionsViewState.value =
            mainViewModel.permissionsViewState.value.copy(SCHEDULE_EXACT_ALARM = permission)

        return permission
    }


    override fun requestPostNotificationPermissions(activity: Activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(activity, postNotificationPermissions,101)
        }
    }

    override fun requestBasePermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity, basePermissions,101)
    }

    override fun requestAlarmPermissions(activity: Activity) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && !alarmManager!!.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.setData(Uri.fromParts("package", context.getPackageName(), null))
            activity.startActivityForResult(intent, 55)
        }
    }


}
