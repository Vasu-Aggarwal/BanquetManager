package com.android.banquetmanager.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.banquetmanager.data.viewmodel.UserViewModel

@Composable
fun AccountScreen(navController: NavController, userViewModel: UserViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Account", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        DashboardScreen(userViewModel = userViewModel, userId = "bLAJolsRfsT4HlKsrZZtPk53opl2", navController = navController)
    }
}

@Composable
fun DashboardScreen(userViewModel: UserViewModel, userId: String, navController: NavController) {
    val context = LocalContext.current
    var userPermissions by remember { mutableStateOf<Map<String, Boolean>?>(null) }
    var userRole by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        try {
            val (permissions, role) = userViewModel.getUserPermissions(userId)
            userPermissions = permissions
            userRole = role
        } catch (e: Exception) {
            errorMessage = "Failed to load user data."
            Log.e("DashboardScreen", "Error fetching permissions", e)
        } finally {
            isLoading = false
        }
    }

    when {
        isLoading -> LoadingScreen()
        errorMessage != null -> ErrorScreen(errorMessage!!)
        userPermissions != null && userRole != null -> {
            DashboardContent(userRole!!, userPermissions!!, navController)
        }
        else -> ErrorScreen("User data not found.")
    }
}

@Composable
fun DashboardContent(userRole: String, userPermissions: Map<String, Boolean>, navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Welcome, $userRole", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        if (userPermissions["canAddUsers"] == true) {
            Button(onClick = { navController.navigate("addUserScreen") }) {
                Text("Add Users")
            }
        }
        if (userPermissions["canCheckBalances"] == true) {
            Button(onClick = { navController.navigate("balancesScreen") }) {
                Text("Check Balances")
            }
        }
        if (userPermissions["canReadData"] == true) {
            Button(onClick = { navController.navigate("viewDataScreen") }) {
                Text("View Data")
            }
        }
        if (userPermissions["canWriteData"] == true) {
            Button(onClick = { navController.navigate("writeDataScreen") }) {
                Text("Write Data")
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Error: $message", color = androidx.compose.ui.graphics.Color.Red, fontSize = 18.sp)
    }
}
