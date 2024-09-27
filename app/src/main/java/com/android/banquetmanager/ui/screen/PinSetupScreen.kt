package com.android.banquetmanager.ui.screen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.android.banquetmanager.utils.AppConstants
import com.android.banquetmanager.utils.UtilityMethods

@Composable
fun PinSetupScreen(onPinSet: (String) -> Unit) {
    val context = LocalContext.current
    var pin by remember { mutableStateOf("") }
    val focusRequesters = remember { List(4) { FocusRequester() } } // One for each digit
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Set up your 4-digit PIN", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Create 4 text fields for each digit of the PIN
            repeat(4) { index ->
                OutlinedTextField(
                    value = pin.getOrNull(index)?.toString() ?: "",
                    onValueChange = { newValue ->
                        if (newValue.length == 1 && newValue.all { it.isDigit() }) {
                            // Update the PIN and move focus to the next box
                            pin = if (index < pin.length) {
                                pin.replaceRange(index, index + 1, newValue)
                            } else {
                                pin + newValue
                            }

                            // Move to the next field or clear focus if this is the last one
                            if (index < 3) {
                                focusRequesters[index + 1].requestFocus()
                            } else {
                                focusManager.clearFocus() // Remove focus after the last box
                            }

                            // Automatically trigger the action when 4 digits are entered
                            if (pin.length == 4) {
                                onPinSet(pin)
                                setPin(context = context, pin)
                            }
                        } else if (newValue.isEmpty()) {
                            // Handle backspace, clear the current box
                            pin = pin.take(index) + pin.drop(index + 1)
                        }
                    },
                    label = { Text("") }, // No label
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = UtilityMethods.PinVisualTransformation(mask = '●'),
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.Black,
                        textAlign = TextAlign.Center // Center the text horizontally
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .align(Alignment.CenterVertically)
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_DEL) {
                                if (pin.getOrNull(index)?.toString().isNullOrEmpty() && index > 0) {
                                    // Move to the previous field if this one is empty
                                    focusRequesters[index - 1].requestFocus()
                                }
                                true
                            } else {
                                false
                            }
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (pin.length == 4) {
                onPinSet(pin)
                setPin(context = context, pin)
            }
        }) {
            Text("Set PIN")
        }

    }
}

fun setPin(context: Context, pin: String) {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    val sharedPreferences = EncryptedSharedPreferences.create(
        AppConstants.SHARED_PREF_KEY,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    sharedPreferences.edit().putString(AppConstants.SHARED_PREF_USER_PIN_KEY, pin).apply()
}
