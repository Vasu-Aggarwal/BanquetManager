package com.android.banquetmanager.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.banquetmanager.ui.component.CalendarScreen

@Composable
fun App(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.CalendarScreen.route){
        composable(route = Screen.CalendarScreen.route){
            CalendarScreen(navController = navController)
        }

        composable(
            route = "dateDetailsScreen/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            eventId?.let {
                DateDetailsScreen(eventId)
            }
        }
    }
}

sealed class Screen(val route: String){
    data object DateDetailsScreen: Screen("dateDetailsScreen/{eventId}"){
        fun createRoute(eventId: String) = "dateDetailsScreen/$eventId"
    }
    data object CalendarScreen: Screen("calendarScreen")
}