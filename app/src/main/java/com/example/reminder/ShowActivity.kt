package com.example.reminder

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.reminder.ui.theme.ReminderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowActivity : ComponentActivity() {

    lateinit var showViewModel: ShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val index = intent.action?.indexOf(":")
        val id = index?.let { intent.action?.substring(it + 1)?.toLong() ?: -1L }

        setContent {

            showViewModel = hiltViewModel()

            if (id != null) {
                showViewModel.reminderId = id
            }

            ReminderTheme {

                LaunchedEffect(Unit) {
                    if (showViewModel.reminderId != -1L) {
                        showViewModel.getReminderById()
                    }

                }

                androidx.compose.material3.Scaffold(
                    modifier = Modifier.fillMaxSize(),

                    topBar = {

                        TopAppBar (
                            backgroundColor = androidx.compose.material.MaterialTheme.colors.background,
                            contentColor = androidx.compose.material.MaterialTheme.colors.primary,

                            title = {
                                Text(stringResource(id = R.string.reminder))
                            },
                            actions = {
                                androidx.compose.material.IconButton(onClick = {
                                    exit()
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
                                    enabled = !isMainActivityInBackStack(),
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {

                                        if (isMainActivityInBackStack()) {
                                            exit()
                                        } else {

                                            val activityIntent = Intent(applicationContext, MainActivity::class.java)
                                            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            ContextCompat.startActivity(applicationContext, activityIntent, null)

                                            exit()
                                        }
                                    },

                                    ) {
                                    Text(stringResource(
                                        id = R.string.show_all_reminders),
                                        color =
                                        if (isMainActivityInBackStack()) Color.LightGray
                                        else Color.Black
                                    )
                                }

                            }
                        }

                    },

                    content = { padding ->

                            if (showViewModel.reminderId == -1L) {
                                Column(
                                    modifier = Modifier
                                        .padding(padding)
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text(stringResource(id = R.string.reminder_not_found))
                                }
                            } else {

                                Column(
                                    modifier = Modifier
                                        .padding(padding)
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {


                                    TextField(
                                        enabled = false,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 4.dp),
                                        label = { Text(stringResource(id = R.string.title)) },
                                        value = showViewModel.reminderTitle,
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
                                        ),
                                        onValueChange = {},
                                    )

                                    TextField(
                                        enabled = false,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 4.dp),
                                        label = { Text(stringResource(id = R.string.user)) },
                                        value = showViewModel.userName,
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
                                        ),
                                        onValueChange = {},
                                    )
                                    TextField(
                                        enabled = false,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 4.dp),
                                        label = { Text(stringResource(id = R.string.email)) },
                                        value = showViewModel.userEmail,
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
                                        ),
                                        onValueChange = {},
                                    )
                                    TextField(
                                        enabled = false,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 4.dp),
                                        label = { Text(stringResource(id = R.string.date)) },
                                        value = showViewModel.reminderDate,
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
                                        ),
                                        onValueChange = {},
                                    )
                                    TextField(
                                        enabled = false,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 4.dp),
                                        label = { Text(stringResource(id = R.string.time)) },
                                        value = showViewModel.reminderTime,
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
                                        ),
                                        onValueChange = {},
                                    )

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 4.dp),
                                    ) {
                                        SubcomposeAsyncImage(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            contentScale = ContentScale.FillWidth,
                                            model = showViewModel.userPictureLarge,
                                            contentDescription = null,


                                            loading = {
                                                CircularProgressIndicator()
                                            },
                                        )
                                    }

                                }

                            }
                        }

                )
            }
        }
    }
    fun exit() {
        this.finish()
    }

    fun isMainActivityInBackStack(): Boolean {

        val mngr = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val taskList = mngr.getRunningTasks(1)

        if (taskList[0].baseActivity!!.className == "com.example.reminder.MainActivity") {
            return true
        } else {
            return false
        }
    }
}