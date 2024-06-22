package com.example.reminder

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reminder.ui.navigation.Navigation
import com.example.reminder.ui.theme.ReminderTheme
import dagger.hilt.android.AndroidEntryPoint

lateinit var mainViewModel: MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var isAppInited: Boolean = false
    var isFistStart: Boolean = true

    private lateinit var toast: Toast

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //

    inner class NotificationReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            when (intent.action) {
                "REMINDER" -> {
                    val value = intent.getStringExtra("notification")

                    //showReminder(value.toString())

                    mainViewModel.notification = value.toString()
                    mainViewModel.showNotification.value = true
                }
                "STATUS" -> {
                    //val value = intent.getBooleanExtra("notification_server_enabled", false)
                }

            }


        }
    }


    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var receiver = NotificationReceiver()
        registerReceiver(receiver, IntentFilter("REMINDER"))
        registerReceiver(receiver, IntentFilter("STATUS"))

        setContent {

            mainViewModel = hiltViewModel()

            mainViewModel.getPermissionsApi().hasAllPermissions(this)

            isAppInited = true

            if (isFistStart) {
                if (mainViewModel.getPermissionsApi().hasAllPermissions(this)) {
                    if (!isServiceRunning()) {
                        val intent = Intent(applicationContext, NotificationService::class.java)
                        intent.putExtra("app_request", "start")
                        startForegroundService(intent)
                    } else {
                        val intent = Intent(applicationContext, NotificationService::class.java)
                        intent.putExtra("app_request", "status")
                        startForegroundService(intent)
                    }

                    isFistStart = false
                }
            }


            if (mainViewModel.exitFromApp.value) { exitFromApp() }

            ReminderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    //modifier = Modifier.fillMaxSize(),
                    //color = MaterialTheme.colorScheme.error
                ) {
                    Navigation(
                        onBackPressed = ::exitFromApponOnBackPressed,
                    )
                }
            }


        }
    }


    fun stopNotificationService() {
        val intent = Intent(this, NotificationService::class.java)
        stopService(intent)

        exit()
    }

    fun exit() {
        this.finish()
    }

    fun exitFromApp() {

        val alertDialog = android.app.AlertDialog.Builder(this)

        alertDialog.apply {
            setIcon(R.drawable.feip)
            setTitle(getApplicationContext().getResources().getString(R.string.app_name))
            setMessage(
                getApplicationContext().getResources()
                    .getString(R.string.continue_working_in_background)
            )
            setPositiveButton(getApplicationContext().getResources().getString(R.string.yes))
            { _: DialogInterface?, _: Int ->  exit() }
            setNegativeButton(getApplicationContext().getResources().getString(R.string.no))
            { _, _ ->  stopNotificationService() }

        }.create().show()


    }

    fun exitFromApponOnBackPressed() {

        val alertDialog = android.app.AlertDialog.Builder(this)

        alertDialog.apply {
            setIcon(R.drawable.feip)
            setTitle(getApplicationContext().getResources().getString(R.string.app_name))
            setMessage(
                getApplicationContext().getResources()
                    .getString(R.string.do_you_really_want_to_close_the_application)
            )
            setPositiveButton(getApplicationContext().getResources().getString(R.string.yes))
            { _: DialogInterface?, _: Int -> exitFromApp() }
            setNegativeButton(getApplicationContext().getResources().getString(R.string.no))
            { _, _ -> }

        }.create().show()
    }


    override fun onResume() {
        super.onResume()
        if (isAppInited) {
            mainViewModel.getPermissionsApi().hasAllPermissions(this)
        }
    }

    fun isServiceRunning(): Boolean {

        val serviceClass: Class<*> = NotificationService::class.java
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        // Loop through the running services
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                // If the service is running then return true
                return true
            }
        }
        return false
    }

    @SuppressLint("ShowToast")
    private fun showReminder(notification: String) {
        toast = Toast.makeText(this, notification, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
        toast.show()
    }


}

