package com.android.banquetmanager.ui.screen

import android.net.Uri
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

        composable(
            route = "addEventBooking/{date}/{slot}",
            arguments = listOf(navArgument("date") { type = NavType.StringType }, navArgument("slot") { type = NavType.StringType })
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date")
            val slot = backStackEntry.arguments?.getString("slot")

            if (date != null && slot != null) {
                AddEventBooking(date = date, slot = slot)
            }
        }
    }
}

sealed class Screen(val route: String){
    data object DateDetailsScreen: Screen("dateDetailsScreen/{eventId}"){
        fun createRoute(eventId: String) = "dateDetailsScreen/$eventId"
    }
    data object CalendarScreen: Screen("calendarScreen")
    data object AddEventBooking: Screen("addEventBooking/{date}/{slot}"){
        fun createRoute(date: String, slot: String): String {
            val encodedDate = Uri.encode(date)
            return "addEventBooking/$encodedDate/$slot"
        }
    }
}