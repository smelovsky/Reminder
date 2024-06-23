package com.example.reminder.ui.screens

import android.Manifest
import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomNavigation
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.reminder.R
import com.example.reminder.mainViewModel


@Composable
fun MainScreen(
   navController : NavController,
   navigateBack: () -> Unit,) {

    val activity = (LocalContext.current as Activity)

    BackHandler(onBack = navigateBack)

    LaunchedEffect(Unit) {
        mainViewModel.getReminderList()
    }

    androidx.compose.material3.Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

            TopAppBar (
                backgroundColor = androidx.compose.material.MaterialTheme.colors.background,
                contentColor = androidx.compose.material.MaterialTheme.colors.primary,
                title = {
                    Text(stringResource(id = R.string.app_name))
                },
                elevation = 0.dp,
                actions = {
                    androidx.compose.material.IconButton(onClick = {
                        mainViewModel.exitFromApp.value = true
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

                                navController.navigate("edit")
                            } else {
                                if (!mainViewModel.getPermissionsApi().hasBasePermissions(activity)
                                ) {
                                    mainViewModel.getPermissionsApi().requestBasePermissions(activity)
                                } else if (!mainViewModel.getPermissionsApi().hasPostNotificationPermissions(activity)
                                ) {
                                    mainViewModel.getPermissionsApi().requestPostNotificationPermissions(activity)
                                }
                            }
                        }
                    ) {
                        if (mainViewModel.permissionsViewState.value.permissionsGranted) {
                            Text(stringResource(R.string.add_new_reminder))
                        } else {
                            Text(stringResource(R.string.permissions))
                        }

                    }
                }
            }

        },

        content = { padding ->
            val lazyListState: LazyListState = rememberLazyListState()
            val showAlertDialog = remember { mutableStateOf(false) }

            if (mainViewModel.permissionsViewState.value.permissionsGranted) {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(Color.Transparent),
                ) {

                    if (mainViewModel.showNotification.value) {

                        val body = "${stringResource(R.string.you_have_a_meeting_with)} ${mainViewModel.notification}"

                        AlertDialog(
                            onDismissRequest = {  },
                            title = {
                                Row() {
                                    Icon(
                                        painterResource(R.drawable.feip),
                                        contentDescription = "AlertDialog",
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Spacer(modifier = Modifier.size(10.dp))
                                    androidx.compose.material.Text(stringResource(R.string.app_name), fontSize = 22.sp)
                                }

                            },

                            text = { androidx.compose.material.Text(body, fontSize = 16.sp) },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        mainViewModel.showNotification.value = false
                                    }
                                ) {
                                    androidx.compose.material.Text(stringResource(R.string.ok))
                                }
                            },
                        )

                    }
                    if (showAlertDialog.value) {
                        AlertDialog(
                            onDismissRequest = {  },
                            title = {
                                Row() {
                                    Icon(
                                        painterResource(R.drawable.feip),
                                        contentDescription = "AlertDialog",
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Spacer(modifier = Modifier.size(10.dp))
                                    androidx.compose.material.Text(stringResource(R.string.app_name), fontSize = 22.sp)
                                }

                            },
                            text = { androidx.compose.material.Text(stringResource(R.string.would_you_like_to_delete_selected_reminder), fontSize = 16.sp) },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        mainViewModel.reminderDeleteSelectedItems()
                                        showAlertDialog.value = false
                                    }
                                ) {
                                    androidx.compose.material.Text(stringResource(R.string.yes))
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showAlertDialog.value = false }
                                ) {
                                    androidx.compose.material.Text(stringResource(R.string.no))
                                }
                            }
                        )
                    }

                    LazyColumn (
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.99f),
                        state = lazyListState,
                    ) {
                        itemsIndexed(
                            items = mainViewModel.reminderListEntity,
                            key = { _, reminderItem -> reminderItem.Id })
                        { index, _ ->

                            ReminderItemBlock(
                                index = index,
                                navController = navController,
                                { showAlertDialog.value = true },
                            )

                        }
                    }
                }
            } else {
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
                        text = "POST_NOTIFICATIONS",
                        color = if (mainViewModel.permissionsViewState.value.POST_NOTIFICATIONS) Color.Blue else Color.Red,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Text(
                        text = "FOREGROUND_SERVICE_SPECIAL_USE",
                        color = if (mainViewModel.permissionsViewState.value.FOREGROUND_SERVICE_SPECIAL_USE) Color.Blue else Color.Red,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Text(
                        text = "FOREGROUND_SERVICE",
                        color = if (mainViewModel.permissionsViewState.value.FOREGROUND_SERVICE) Color.Blue else Color.Red,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )


                }

            }


        }
    )

}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun ReminderItemBlock(
    index: Int,
    navController : NavController,
    showAlertDialog: ()-> Unit,

) {

    val details = mainViewModel.reminderListEntity[index]

    val picture = details.PictureLarge
    val isSelected = details.isSelected

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .combinedClickable(

                onClick = {
                    if (details.isSelected) {
                        mainViewModel.reminderUnselectAllItems()
                    } else {
                        mainViewModel.reminderSelectItem(id = details.Id, isSelected = true)
                    }

                },

                )

    ) {
        val (photoRef,
            titleRef,
            deleteIconRef,
            editIconRef,
            nameEmailRef,
            dateTimeRef,
            topDividerRef,
            bottomDividerRef) = createRefs()

        val dividerColor: Color = if (isSelected) {
                androidx.compose.material3.MaterialTheme.colorScheme.secondary
            } else {
                Color.Transparent
            }


        val dividerThickness = 1.5.dp
        Divider(
            color = dividerColor,
            thickness = dividerThickness,
            modifier = Modifier
                .constrainAs(topDividerRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        Divider(
            color = dividerColor,
            thickness = dividerThickness,
            modifier = Modifier
                .constrainAs(bottomDividerRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Box(modifier = Modifier
            .constrainAs(photoRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start, margin = 16.dp)
            },
        ) {
            SubcomposeAsyncImage(
                model = picture,
                contentDescription = null,


                loading = {
                    CircularProgressIndicator()
                },
            )
        }

        Text(
            text = "${details.Title}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(titleRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top)
                    start.linkTo(photoRef.end, margin = if (isSelected) 8.dp else 16.dp )
                    end.linkTo(deleteIconRef.start, )
                }
        )
        Text(
            text = "${details.Name}, ${details.Email}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(nameEmailRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(titleRef.bottom)
                    start.linkTo(photoRef.end, margin = if (isSelected) 8.dp else 16.dp )
                    end.linkTo(deleteIconRef.start, )
                }
        )
        Text(
            text = "${details.Date}, ${details.Time}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(dateTimeRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(nameEmailRef.bottom)
                    start.linkTo(photoRef.end, margin = if (isSelected) 8.dp else 16.dp )
                    end.linkTo(parent.end, )
                }
        )

        if (isSelected) {

            IconButton(
                onClick = { showAlertDialog() },
                modifier = Modifier
                    .constrainAs(deleteIconRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(editIconRef.start, margin = 4.dp)
                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "Delete",
                )
            }

            IconButton(
                onClick = {

                    mainViewModel.reminderId = details.Id
                    mainViewModel.reminderTitle = details.Title
                    mainViewModel.reminderDate = details.Date
                    mainViewModel.reminderTime = details.Time
                    mainViewModel.userName = details.Name
                    mainViewModel.userEmail = details.Email
                    mainViewModel.userPictureLarge = details.PictureLarge
                    mainViewModel.userPictureThumbnail = details.PictureThumbnail

                    navController.navigate("edit")
                },
                modifier = Modifier
                    .constrainAs(editIconRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, )
                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = "Edit",
                )
            }

        }
    }

}