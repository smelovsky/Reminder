package com.example.reminder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reminder.ui.screens.EditScreen
import com.example.reminder.ui.screens.MainScreen
import com.example.reminder.ui.screens.UserScreen

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    onBackPressed: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(navController,
                navigateBack = onBackPressed,)
        }
        composable("edit") {
            EditScreen(navController)
        }
        composable("user") {
            UserScreen(navController)
        }
    }
}
