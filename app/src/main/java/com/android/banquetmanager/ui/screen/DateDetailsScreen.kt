package com.android.banquetmanager.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.repository.BookingRepository
import com.android.banquetmanager.data.viewmodel.BookingViewmodel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun DateDetailsScreen (date: String, viewModel: BookingViewmodel = hiltViewModel()) {

    var events: List<Event> = emptyList()

    LaunchedEffect(date) {
        events = viewModel.getBookingsByDate(date)
        Log.d("Booking date I found: ", "DateDetailsScreen: ${events.getOrNull(0)}")
    }

    Text(text = "This is details for $date", modifier = Modifier.padding(top = 20.dp))
}