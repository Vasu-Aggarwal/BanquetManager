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

        composable(route = Screen.DateDetailsScreen.route,
            arguments = listOf(navArgument("date") { type = NavType.StringType })
        )
        { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date")
            date?.let {
                DateDetailsScreen(it)
            }
        }
    }

}

sealed class Screen(val route: String){
    data object DateDetailsScreen: Screen("dateDetailsScreen/{selectedDate}"){
        fun createRoute(selectedDate: String) = "dateDetailsScreen/$selectedDate"
    }
    data object CalendarScreen: Screen("calendarScreen")
}