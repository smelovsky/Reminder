package com.example.reminder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reminder.ui.screens.EditScreen
import com.example.reminder.ui.screens.MainScreen
import com.example.reminder.ui.screens.PermissionsScreen
import com.example.reminder.ui.screens.UserScreen

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    onBackPressed: () -> Unit,
    onExitPressed: () -> Unit,
    startDestination: String,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("main") {
            MainScreen(
                navController,
                navigateBack = onBackPressed,
                onExitPressed = onExitPressed,
            )
        }
        composable("permissions") {
            PermissionsScreen(navController,
                navigateBack = onBackPressed,
                onExitPressed = onExitPressed,
            )
        }
        composable("edit") {
            EditScreen(
                navController,
            )
        }
        composable("user") {
            UserScreen(navController)
        }
    }
}

fun NavController.popBackStackSafe(rote: String) {
    if (currentDestination?.route == rote) {
        popBackStack()
    }
}

