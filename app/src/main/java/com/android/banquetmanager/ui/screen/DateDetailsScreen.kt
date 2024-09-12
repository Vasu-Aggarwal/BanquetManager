package com.android.banquetmanager.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.repository.BookingRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun DateDetailsScreen (date: String) {

    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var bookingRepository: BookingRepository
    var events: List<Event>

    LaunchedEffect(Unit) {
        events = bookingRepository.getBookingsByDate(date)
        Log.d("Booking date I found: ", "DateDetailsScreen: ${events[0]}")
    }
    Text(text = "This is details ${date}", modifier = Modifier.padding(top = 20.dp))
}