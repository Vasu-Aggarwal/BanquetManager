package com.android.banquetmanager.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.viewmodel.BookingViewmodel

@Composable
fun DateDetailsScreen(
    eventId: String,
    bookingViewmodel: BookingViewmodel = hiltViewModel()
) {
    var event by remember { mutableStateOf<Event?>(null) }

    LaunchedEffect(eventId) {
        event = bookingViewmodel.getBookingByEventId(eventId)
    }

    // Display the first event if it exists
    event?.let {
        EventDetailsCard(event = it)
    } ?: run {
        // Show if no events are found for the selected date
        Text(
            text = "No events found for $eventId",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}

@Composable
fun EventDetailsCard(event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Event Details",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            EventDetailRow(label = "Event ID", value = event.eventId)
            EventDetailRow(label = "Banquet Location", value = event.banquetLocation)
            EventDetailRow(label = "Function Type", value = event.functionType)
            EventDetailRow(label = "Food Type", value = event.foodType)
            EventDetailRow(label = "Pax", value = event.pax.toString())
            EventDetailRow(label = "Package Amount", value = "₹${event.packageAmount}")
            EventDetailRow(label = "Cocktail", value = if (event.cocktail) "Yes" else "No")
            if (event.cocktail)
                EventDetailRow(label = "Cocktail amount", value = event.cocktailAmount.toString())
            EventDetailRow(label = "DJ", value = if (event.dj) "Yes" else "No")
            EventDetailRow(label = "Flower Decoration", value = if (event.flower) "Yes" else "No")

            Spacer(modifier = Modifier.height(8.dp))

            Divider()

            Spacer(modifier = Modifier.height(8.dp))

            EventDetailRow(label = "Date Booked", value = event.dateBooked)
            EventDetailRow(label = "Created On", value = event.createdOn.toDate().toString())

            Spacer(modifier = Modifier.height(8.dp))

            // Add more details if needed
        }
    }
}

@Composable
fun EventDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label: ",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        Text(text = value, modifier = Modifier.weight(1f))
    }
}
