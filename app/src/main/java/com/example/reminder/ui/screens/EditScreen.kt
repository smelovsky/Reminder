package com.example.reminder.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reminder.R
import com.example.reminder.mainViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.BottomNavigation
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(navController : NavController) {

    val focusManager = LocalFocusManager.current

    androidx.compose.material3.Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

            TopAppBar (
                backgroundColor = androidx.compose.material.MaterialTheme.colors.background,
                contentColor = androidx.compose.material.MaterialTheme.colors.primary,
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                title = {
                    if (mainViewModel.reminderId == -1L)
                        Text(stringResource(id = R.string.new_screen_title))
                    else
                        Text(stringResource(id = R.string.edit_screen_title))
                },
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


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {

                        TextButton(
                            enabled =
                                !mainViewModel.reminderTitle.isEmpty() &&
                                !mainViewModel.reminderDate.isEmpty() &&
                                !mainViewModel.userName.isEmpty(),

                            onClick = {

                                if (mainViewModel.reminderId == -1L) {
                                    mainViewModel.insertReminder(
                                        title = mainViewModel.reminderTitle,
                                        name = mainViewModel.userName,
                                        email = mainViewModel.userEmail,
                                        date = mainViewModel.reminderDate.toString(),
                                        time = mainViewModel.reminderTime.toString(),
                                    )
                                } else {
                                    mainViewModel.updateReminder(
                                        id = mainViewModel.reminderId,
                                        title = mainViewModel.reminderTitle,
                                        name = mainViewModel.userName,
                                        email = mainViewModel.userEmail,
                                        date = mainViewModel.reminderDate.toString(),
                                        time = mainViewModel.reminderTime.toString(),
                                        isNotified = false,
                                        isSelected = true,
                                    )
                                }

                                navController.popBackStack()
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.save),
                                color =
                                    if (
                                        mainViewModel.reminderTitle.isEmpty() ||
                                        mainViewModel.reminderDate.isEmpty() ||
                                        mainViewModel.userName.isEmpty()) Color.LightGray
                                    else Color.Black
                            )
                        }
                    }
                }
            }



        },
        content = { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),

                    label = { Text(stringResource(id = R.string.title)) },
                    value = mainViewModel.reminderTitle,
                    isError = mainViewModel.reminderTitle.isEmpty(),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus(true)
                        },
                    ),
                    onValueChange = {
                            newValue ->

                        mainViewModel.reminderTitle = newValue
                    },
                )



                UserPicker(stringResource(id = R.string.user), mainViewModel.userName, {

                    navController.navigate("user" )
                })

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { focusManager.clearFocus(true) }
                        .padding(horizontal = 4.dp),

                    label = { Text(stringResource(id = R.string.email)) },
                    value = mainViewModel.userEmail,
                    enabled = false,
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = if (mainViewModel.userEmail.isEmpty()) Color.Red else MaterialTheme.colorScheme.onSurface,
                    ),
                    onValueChange = {},
                )


                CustomDatePicker(
                    value = mainViewModel.reminderDate,
                    focusManager = focusManager,
                    onValueChange = { mainViewModel.reminderDate = it }
                )

                TimePicker(
                    value = mainViewModel.reminderTime,
                    focusManager = focusManager,
                    onValueChange = {
                        mainViewModel.reminderTime = it
                    }
                )

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { focusManager.clearFocus(true) }
                    .padding(horizontal = 4.dp),

                ) {
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillWidth,
                        model = mainViewModel.userPictureLarge,
                        contentDescription = null,


                        loading = {
                            CircularProgressIndicator()
                        },
                    )
                }

            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    value: String,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit
) {

    val open = remember { mutableStateOf(false)}

    var date =  LocalDate.now()

    if (open.value) {
        CalendarDialog(
            state = rememberUseCaseState(visible = true, true, onCloseRequest = { open.value = false } ),
            config = CalendarConfig(
                yearSelection = true,
                style = CalendarStyle.MONTH,
            ),
            selection = CalendarSelection.Date(
                selectedDate = date
            ) { newDate ->
                onValueChange(newDate.toString())
            },
        )
    }

    TextField(
        label = { Text(stringResource(id = R.string.date)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable {
                focusManager.clearFocus(true)
                open.value = true
            },
        enabled = false,// <- Add this to make click event work
        value = value.format(DateTimeFormatter.ISO_DATE) ,
        onValueChange = {},

        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = if (value.isEmpty()) Color.Red else MaterialTheme.colorScheme.onSurface,
        )
    )
}

@Composable
fun TimePicker(
    value: String,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    pattern: String = "HH:mm",
    is24HourView: Boolean = true,
) {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val time = if (value.isNotBlank()) LocalTime.parse(value, formatter) else LocalTime.now()
    val dialog = TimePickerDialog(
        LocalContext.current,
        { _, hour, minute -> onValueChange(LocalTime.of(hour, minute).toString()) },
        time.hour,
        time.minute,
        is24HourView,
    )

    TextField(
        label = { Text(stringResource(id = R.string.time)) },
        value = value,
        onValueChange = onValueChange,
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable {
                focusManager.clearFocus(true)
                dialog.show()
            },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
        )
    )
}

@Composable
fun UserPicker(
    label: String,
    value: String,
    onClick: () -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    TextField(
        label = { Text(label) },
        value = value,
        onValueChange = {},
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable {
                onClick()
            },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = if (mainViewModel.userName.isEmpty()) Color.Red else MaterialTheme.colorScheme.onSurface,
        )
    )
}

