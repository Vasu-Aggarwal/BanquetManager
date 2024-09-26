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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

@Composable
fun PinSetupScreen(onPinSet: (String) -> Unit) {
    val context = LocalContext.current
    var pin by remember { mutableStateOf("") }

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
                    value = if (index < pin.length) pin[index].toString() else "",
                    onValueChange = { newValue ->
                        // Ensure only single digit input for each field
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                            // Update pin state
                            pin = pin.replaceRange(index, index + 1, newValue)
                            if (newValue.isEmpty() && pin.isNotEmpty()) {
                                // Remove character when backspace is pressed
                                pin = pin.removeRange(index, index + 1)
                            }
                        }
                    },
                    label = { Text("") }, // No label
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp) // Space between text fields
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
        "secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    sharedPreferences.edit().putString("user_pin", pin).apply()
}
