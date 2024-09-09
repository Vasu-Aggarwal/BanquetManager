package com.android.banquetmanager.ui.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun DateDetailsScreen(selectedDate: String) {
    Text(text = "This is details $selectedDate")
}