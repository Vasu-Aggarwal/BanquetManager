package com.android.banquetmanager.ui.screen

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.banquetmanager.ui.component.BottomNavigation
import com.android.banquetmanager.ui.component.CalendarScreen

@Composable
fun App(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.BottomNavigationBar.route) {
        composable(route = Screen.BottomNavigationBar.route) {
            BottomNavigation(navAppController = navController)
        }
    }
}

sealed class Screen(val route: String){

    data object BottomNavigationBar: Screen("bottomBar")
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