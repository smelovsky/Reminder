package com.example.reminder.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.reminder.mainViewModel

val basePermissions = arrayOf(
    Manifest.permission.INTERNET,
    Manifest.permission.ACCESS_NOTIFICATION_POLICY,
)

val postNotificationPermissions = arrayOf(
    Manifest.permission.POST_NOTIFICATIONS,
)

val accessBackgroundLocationPermissions = arrayOf(
    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
)

data class PermissionsViewState(
    val INTERNET: Boolean = false,
    val ACCESS_BACKGROUND_LOCATION: Boolean = false,
    val POST_NOTIFICATIONS: Boolean = false,
    val ACCESS_NOTIFICATION_POLICY: Boolean = false,

    val permissionsGranted: Boolean = false,
)

class PermissionsImpl(val context: Context): PermissionsApi {

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

        if (!hasAccessBackgroundLocationPermissions(activity)) {
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

            }
        }

        return result
    }

    override fun hasAccessBackgroundLocationPermissions(activity: Activity): Boolean{
        var result = true
        accessBackgroundLocationPermissions.forEach {

            val permission = ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            if ( !permission)
            {
                result = false
            }
            when (it) {

                Manifest.permission.ACCESS_BACKGROUND_LOCATION -> mainViewModel.permissionsViewState.value =
                    mainViewModel.permissionsViewState.value.copy(ACCESS_BACKGROUND_LOCATION = permission)

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(activity, postNotificationPermissions,101)
        } else {
        }
    }

    override fun requestBasePermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity, basePermissions,101)
    }

    override fun requestAccessBackgroundLocationPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity, accessBackgroundLocationPermissions,101)
    }

}
