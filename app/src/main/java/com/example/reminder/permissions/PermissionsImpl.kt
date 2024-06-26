package com.example.reminder.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.reminder.mainViewModel

var basePermissions = arrayOf(
    Manifest.permission.INTERNET,
    Manifest.permission.ACCESS_NOTIFICATION_POLICY,
    Manifest.permission.FOREGROUND_SERVICE,
)

val postNotificationPermissions = arrayOf(
    Manifest.permission.POST_NOTIFICATIONS,
)

data class PermissionsViewState(
    val INTERNET: Boolean = false,
    val POST_NOTIFICATIONS: Boolean = false,
    val ACCESS_NOTIFICATION_POLICY: Boolean = false,
    val FOREGROUND_SERVICE: Boolean = false,
    val FOREGROUND_SERVICE_SPECIAL_USE: Boolean = false,

    val permissionsGranted: Boolean = false,
)

class PermissionsImpl(val context: Context): PermissionsApi {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            basePermissions = arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE,
                Manifest.permission.FOREGROUND_SERVICE,
            )
        }
    }

    private fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }

    override fun hasAllPermissions(activity: Activity): Boolean{

        var result = true

        if (!hasBasePermissions(activity)) {
            result = false
        }

        if (!hasPostNotificationPermissions(activity)) {
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
                Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE -> mainViewModel.permissionsViewState.value =
                    mainViewModel.permissionsViewState.value.copy(FOREGROUND_SERVICE_SPECIAL_USE = permission)
                Manifest.permission.FOREGROUND_SERVICE -> mainViewModel.permissionsViewState.value =
                    mainViewModel.permissionsViewState.value.copy(FOREGROUND_SERVICE = permission)

            }
        }

        return result
    }

    override fun hasPostNotificationPermissions(activity: Activity): Boolean{

        val permission = //true // TODO

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


    override fun requestPostNotificationPermissions(activity: Activity) {
        Log.d("zzz", "requestPostNotificationPermissions")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(activity, postNotificationPermissions,101)
        } else {
        }
    }

    override fun requestBasePermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity, basePermissions,101)
    }


}
