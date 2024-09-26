package com.android.banquetmanager.ui.screen

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.android.banquetmanager.ui.component.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    navAppController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var showBiometricOption by remember { mutableStateOf(false) }

    val appContext = ComponentActivity()

    // Check for biometric availability
    LaunchedEffect(Unit) {
        showBiometricOption = isBiometricAvailable(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Input Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

//         Biometric login button (only show if biometric option is available)
//        if (showBiometricOption) {
//            Button(
//                enabled = showBiometricOption,
//                onClick = {
//                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
//                    showBiometricPrompt(context, onAuthenticated = {
//                        Toast.makeText(context, "Biometric authentication successful!", Toast.LENGTH_SHORT).show()
//                    }, onError = { error ->
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
//                    })
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Login with Biometrics")
//            }
//        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = {
                isLoading = true
                errorMessage = null
                performLogin(email, password,
                    onLoginSuccess = {
                        isLoading = false
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                        // Navigate to the next screen
                        navAppController.navigate(Screen.PinSetupScreen.route)
                    },
                    onLoginFailure = { error ->
                        isLoading = false
                        errorMessage = error
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Login")
        }

        // Error Message Display
        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = Color.Red)
        }

        // Loading Indicator
        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}

// Function to perform Firebase Email/Password login
fun performLogin(
    email: String,
    password: String,
    onLoginSuccess: () -> Unit,
    onLoginFailure: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onLoginSuccess()
            } else {
                val error = task.exception?.localizedMessage ?: "Login failed"
                Log.e("LoginScreen", "Login failed: $error")
                onLoginFailure(error)
            }
        }
}

// Function to check if biometric authentication is available
fun isBiometricAvailable(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
        BiometricManager.BIOMETRIC_SUCCESS -> true
        else -> false
    }
}

// Biometric prompt function
fun showBiometricPrompt(context: Context, onAuthenticated: () -> Unit, onError: (String) -> Unit) {
    val activity = getActivity(context)
    if (activity == null) {
        onError("Activity is null")
        return
    }

    val executor = ContextCompat.getMainExecutor(context)

    val biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onAuthenticated()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(errString.toString())
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onError("Authentication failed")
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Login")
        .setSubtitle("Authenticate using your biometric credential")
        .setNegativeButtonText("Cancel")
        .build()

    biometricPrompt.authenticate(promptInfo)
}

fun getActivity(context: Context): AppCompatActivity? {
    var currentContext = context
    while (currentContext is ContextWrapper) {
        if (currentContext is AppCompatActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}
