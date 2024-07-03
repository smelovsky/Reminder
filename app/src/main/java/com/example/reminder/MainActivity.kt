package com.example.reminder

import android.app.AlarmManager
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reminder.ui.navigation.Navigation
import com.example.reminder.ui.theme.ReminderTheme
import dagger.hilt.android.AndroidEntryPoint


lateinit var mainViewModel: MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var isAppInited: Boolean = false

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            mainViewModel = hiltViewModel()

            mainViewModel.getPermissionsApi().hasAllPermissions(this)

            isAppInited = true

            ReminderTheme {

                Navigation(
                    onBackPressed = ::exitFromApponOnBackPressed,
                    onExitPressed = ::exit,
                    startDestination =
                    if (!mainViewModel.permissionsViewState.value.permissionsGranted) "permissions"
                    else "main",
                )
            }

        }
    }

    fun exit() {
        isAppInited = false
        this.finish()
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
            { _: DialogInterface?, _: Int -> exit() }
            setNegativeButton(getApplicationContext().getResources().getString(R.string.no))
            { _, _ -> }

        }.create().show()

    }


    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()

        if (isAppInited) {
            mainViewModel.getPermissionsApi().hasAllPermissions(this)
        }
    }
}

