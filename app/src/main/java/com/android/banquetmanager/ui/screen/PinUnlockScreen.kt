package com.android.banquetmanager.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.android.banquetmanager.utils.AppConstants

@Composable
fun PinUnlockScreen(onUnlock: () -> Unit) {
    val context = LocalContext.current
    val storedPin = getPin(context) ?: ""

    var pin by remember { mutableStateOf(List(4) { "" }) }
    val focusRequesters = remember { List(4) { FocusRequester() } }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter your 4-digit PIN", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(pin.indices.toList()) { index ->
                OutlinedTextField(
                    value = pin[index],
                    onValueChange = { newValue ->
                        if (newValue.length == 1 && newValue.all { it.isDigit() }) {
                            pin = pin.toMutableList().apply { this[index] = newValue }

                            if (index < 3) {
                                focusRequesters[index + 1].requestFocus()
                            } else {
                                focusManager.clearFocus()
                                verifyPin(context, pin.joinToString(""), storedPin, onUnlock) {
                                    // Clear input and refocus on error
                                    coroutineScope.launch {
                                        delay(500) // Show error briefly before clearing
                                        pin = List(4) { "" }
                                        focusRequesters[0].requestFocus()
                                    }
                                }
                            }
                        } else if (newValue.isEmpty()) {
                            pin = pin.toMutableList().apply { this[index] = "" }
                            if (index > 0) focusRequesters[index - 1].requestFocus()
                        }
                    },
                    label = { Text("") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .width(60.dp)
                        .focusRequester(focusRequesters[index])
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                verifyPin(context, pin.joinToString(""), storedPin, onUnlock) {
                    coroutineScope.launch {
                        delay(500)
                        pin = List(4) { "" }
                        focusRequesters[0].requestFocus()
                    }
                }
            },
            enabled = pin.joinToString("").length == 4
        ) {
            Text("Unlock")
        }
    }

    // Automatically focus the first field on launch
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}

fun verifyPin(context: Context, enteredPin: String, storedPin: String, onUnlock: () -> Unit, onError: () -> Unit) {
    if (enteredPin == storedPin) {
        Toast.makeText(context, "Unlock successful", Toast.LENGTH_SHORT).show()
        onUnlock()
    } else {
        Toast.makeText(context, "Incorrect PIN", Toast.LENGTH_SHORT).show()
        onError()
    }
}

fun getPin(context: Context): String? {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    val sharedPreferences = EncryptedSharedPreferences.create(
        AppConstants.SHARED_PREF_KEY,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    return sharedPreferences.getString(AppConstants.SHARED_PREF_USER_PIN_KEY, null)
}
