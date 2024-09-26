package com.android.banquetmanager.ui.component

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.android.banquetmanager.ui.screen.LoginScreen
import com.android.banquetmanager.ui.screen.PinSetupScreen
import com.android.banquetmanager.ui.screen.PinUnlockScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun App() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination =
        if (currentUser?.uid != null){
            if (getPin(context) != null){
                Screen.PinUnlockScreen.route
            } else {
                Screen.PinSetupScreen.route
            }
        }
        else{
            Screen.LoginScreen.route
        }
    ) {
        composable(route = Screen.PinSetupScreen.route) {
            PinSetupScreen{ pin ->
                navController.navigate(Screen.PinUnlockScreen.route)
            }
        }

        composable(route = Screen.PinUnlockScreen.route) {
            PinUnlockScreen {
                navController.navigate(Screen.BottomNavigationBar.route)
            }
        }

        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navAppController = navController)
        }

        composable(route = Screen.BottomNavigationBar.route) {
            BottomNavigation(navAppController = navController)
        }
    }
}

sealed class Screen(val route: String){

    data object BottomNavigationBar: Screen("bottomBar")
    data object LoginScreen: Screen("loginScreen")
    data object PinSetupScreen: Screen("pinSetupScreen")
    data object PinUnlockScreen: Screen("pinUnlockScreen")

    data object DateDetailsScreen: Screen("dateDetailsScreen/{eventId}"){
        fun createRoute(eventId: String) = "dateDetailsScreen/$eventId"
    }

    data object CalendarScreen: Screen("calendarScreen")

    data object FilterScreen: Screen("filterScreen")

    data object AccountScreen: Screen("accountScreen")

    data object AddEventBooking: Screen("addEventBooking/{date}/{slot}"){
        fun createRoute(date: String, slot: String): String {
            val encodedDate = Uri.encode(date)
            return "addEventBooking/$encodedDate/$slot"
        }
    }
}

fun getPin(context: Context): String? {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    val sharedPreferences = EncryptedSharedPreferences.create(
        "secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    return sharedPreferences.getString("user_pin", null)
}