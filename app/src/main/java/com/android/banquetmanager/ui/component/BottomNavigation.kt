package com.android.banquetmanager.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.banquetmanager.ui.screen.AccountScreen
import com.android.banquetmanager.ui.screen.AddEventBooking
import com.android.banquetmanager.ui.screen.DateDetailsScreen
import com.android.banquetmanager.ui.screen.FilterScreen

@Composable
fun BottomNavigation(navAppController: NavController) {

    val bottomItems = listOf(
        BottomNavigationItem(
            title = "Calendar",
            selectedIcon = Icons.Filled.CalendarMonth,
            unselectedItem = Icons.Outlined.CalendarMonth,
            hasNews = false
        ),

        BottomNavigationItem(
            title = "Filter",
            selectedIcon = Icons.Filled.FilterAlt,
            unselectedItem = Icons.Outlined.FilterAlt,
            hasNews = false
        ),

        BottomNavigationItem(
            title = "Account",
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedItem = Icons.Outlined.AccountCircle,
            hasNews = false
        )
    )

    val navController = rememberNavController()
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }


    Scaffold(bottomBar = {
        val currentBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentDestination = currentBackStackEntry?.destination?.route
        Column(
            modifier = Modifier.padding(bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding())
        ){
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
            BottomNavigation(
                backgroundColor = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                bottomItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        modifier = Modifier.align(Alignment.CenterVertically).height(25.dp),
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(
                                when (index) {
                                    0 -> Screen.CalendarScreen.route
                                    1 -> Screen.FilterScreen.route
                                    2 -> Screen.AccountScreen.route
                                    else -> Screen.CalendarScreen.route
                                }
                            )
                        },
                        label = {
                            Text(text = item.title)
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.badgeCount != null) {
                                        Badge {
                                            Text(text = item.badgeCount.toString())
                                        }
                                    } else if (item.hasNews) {
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex)
                                        item.selectedIcon
                                    else
                                        item.unselectedItem, contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
        }
    }){ innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.CalendarScreen.route,
                modifier = Modifier.padding(innerPadding.calculateStartPadding(LocalLayoutDirection.current), 0.dp, innerPadding.calculateEndPadding(
                LocalLayoutDirection.current), innerPadding.calculateBottomPadding())
            ){
                composable(route = Screen.CalendarScreen.route){
                    CalendarScreen(navController = navController)
                }

                composable(route = Screen.FilterScreen.route){
                    FilterScreen(navController = navController)
                }

                composable(route = Screen.AccountScreen.route){
                    AccountScreen(navController = navController)
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
                        AddEventBooking(date = date, slot = slot, navController)
                    }
                }
            }
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedItem: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)