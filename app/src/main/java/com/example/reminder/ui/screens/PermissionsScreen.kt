package com.example.reminder.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reminder.R
import com.example.reminder.mainViewModel

@Composable
fun PermissionsScreen(
    navController : NavController,
    navigateBack: () -> Unit,
    onExitPressed: () -> Unit,
    ) {

    val activity = (LocalContext.current as Activity)

    BackHandler(onBack = navigateBack)


    androidx.compose.material3.Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

            TopAppBar (
                backgroundColor = androidx.compose.material.MaterialTheme.colors.background,
                contentColor = androidx.compose.material.MaterialTheme.colors.primary,
                title = {
                    Text(stringResource(id = R.string.permissions_screen_title))
                },
                elevation = 0.dp,
                actions = {
                    androidx.compose.material.IconButton(onClick = {
                        onExitPressed()
                    }) {
                        androidx.compose.material.Icon(
                            Icons.Outlined.ExitToApp,
                            contentDescription = "Exit",
                        )
                    }
                }
            )
        },


        bottomBar = {

            BottomNavigation(
                backgroundColor = androidx.compose.material.MaterialTheme.colors.background,
                contentColor = androidx.compose.material.MaterialTheme.colors.primary,
                elevation = 0.dp,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {


                    TextButton(

                        onClick = {

                            if (mainViewModel.permissionsViewState.value.permissionsGranted) {
                                mainViewModel.reminderId = -1L
                                mainViewModel.reminderTitle = ""
                                mainViewModel.reminderDate = ""
                                mainViewModel.reminderTime = ""
                                mainViewModel.userName = ""
                                mainViewModel.userEmail = ""
                                mainViewModel.userPictureLarge = ""
                                mainViewModel.userPictureThumbnail = ""

                                navController.navigate("main")
                            } else {
                                if (!mainViewModel.getPermissionsApi().hasBasePermissions(activity)
                                ) {
                                    mainViewModel.getPermissionsApi().requestBasePermissions(activity)
                                } else if (!mainViewModel.getPermissionsApi().hasPostNotificationPermissions(activity)
                                ) {
                                    mainViewModel.getPermissionsApi().requestPostNotificationPermissions(activity)
                                } else if (!mainViewModel.getPermissionsApi().hasAlarmPermissions(activity)
                                ) {
                                    mainViewModel.getPermissionsApi().requestAlarmPermissions(activity)
                                }
                            }
                        }
                    ) {
                        Text(stringResource(R.string.permissions))
                    }
                }
            }

        },

        content = { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "INTERNET",
                    color = if (mainViewModel.permissionsViewState.value.INTERNET) Color.Blue else Color.Red,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Text(
                    text = "ACCESS_NOTIFICATION_POLICY",
                    color = if (mainViewModel.permissionsViewState.value.ACCESS_NOTIFICATION_POLICY) Color.Blue else Color.Red,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Text(
                    text = "SCHEDULE_EXACT_ALARM",
                    color = if (mainViewModel.permissionsViewState.value.SCHEDULE_EXACT_ALARM) Color.Blue else Color.Red,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Text(
                    text = "POST_NOTIFICATIONS",
                    color = if (mainViewModel.permissionsViewState.value.POST_NOTIFICATIONS) Color.Blue else Color.Red,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

            }
        }
    )

}
