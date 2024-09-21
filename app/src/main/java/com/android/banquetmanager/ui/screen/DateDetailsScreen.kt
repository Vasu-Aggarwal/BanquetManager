package com.android.banquetmanager.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.banquetmanager.R
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.model.Payment
import com.android.banquetmanager.data.viewmodel.BookingViewmodel
import com.android.banquetmanager.utils.AppConstants
import com.android.banquetmanager.utils.BanquetLocations
import com.android.banquetmanager.utils.FoodType
import com.android.banquetmanager.utils.FunctionType
import com.android.banquetmanager.utils.UtilityMethods

@Composable
fun DateDetailsScreen(
    eventId: String,
    bookingViewmodel: BookingViewmodel = hiltViewModel()
) {
    var event by remember { mutableStateOf<Event?>(null) }
    val showPaymentDetails by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(eventId) {
        event = bookingViewmodel.getBookingByEventId(eventId)
    }

    // Display the first event if it exists
    event?.let {
        EventDetailsCard(event = it, bookingViewmodel, showPaymentDetails)
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
fun EventDetailsCard(event: Event, bookingViewmodel: BookingViewmodel, showPaymentDetails: Boolean) {

    var paymentDetails by remember {
        mutableStateOf(emptyList<Payment>())
    }

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
                fontSize = AppConstants.HEADING_TEXT.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            EventDetailRow(label = "Banquet Location", value = event.banquetLocation.let { BanquetLocations.fromName(it)?.displayName })
            EventDetailRow(label = "Function Type", value = event.functionType.let { FunctionType.fromName(it)?.displayName })
            EventDetailRow(
                label = "Food Type",
                value = event.foodType.let { FoodType.fromName(it)?.displayName },
                image = if (event.foodType.equals(FoodType.VEG.name))
                            IconType.PainterIcon(painterResource(id = R.drawable.vegetarian))
                        else
                            IconType.PainterIcon(painterResource(id = R.drawable.non_vegetarian))
            )
            EventDetailRow(label = "Pax", value = event.pax.toString(), icon = IconType.ImageVectorIcon(Icons.Default.Person))
            EventDetailRow(label = "Package Amount", value = UtilityMethods.formatAmount(event.packageAmount))
            EventDetailRow(label = "Cocktail", value = if (event.cocktail) "Yes" else "No")
            if (event.cocktail)
                EventDetailRow(label = "Cocktail amount", value = UtilityMethods.formatAmount(event.cocktailAmount))
            EventDetailRow(label = "DJ", value = if (event.dj) "Yes" else "No")
            EventDetailRow(label = "Flower Decoration", value = if (event.flower) "Yes" else "No")

            //Payment details and extra plates
            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            //Show the payment details only when showPaymentdetails flag is true (the flag is controlled on the filters page)
            if (showPaymentDetails) {
                //If extra plates are added then only show otherwise hide
                if (event.extraPlate != 0L)
                    EventDetailRow(label = "Extra Plates", value = event.extraPlate.toString())

                //All the payments received
                if (event.paymentDetail.isNotEmpty()) {
                    LaunchedEffect(Unit) {
                        paymentDetails =
                            bookingViewmodel.getPaymentDetailsByEvent(event.paymentDetail)
                    }

                    paymentDetails.forEachIndexed { i, payment ->
                        EventDetailRow(label = "Payment ${i + 1}", value = "")
                        EventDetailRow(label = "Description", value = payment.paymentDescription)
                        EventDetailRow(
                            label = "Amount",
                            value = UtilityMethods.formatAmount(payment.amount)
                        )
                        EventDetailRow(label = "Mode", value = payment.paymentMode)
                    }

                }
            }

            EventDetailRow(label = "Balance", value = UtilityMethods.formatAmount(event.balance.toDouble()))

            Spacer(modifier = Modifier.height(8.dp))

            //Booking added date
            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            EventDetailRow(label = "Date Booked", value = event.dateBooked)
            EventDetailRow(label = "Created On", value = event.createdOn.toDate().toLocaleString())

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun EventDetailRow(label: String, value: String?, icon: IconType? = null, image: IconType? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically // Center-align the icon with text
    ) {
        Text(
            text = "$label: ",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value ?: "N/A"
            )
            if (icon != null) {
                when (icon) {
                    is IconType.ImageVectorIcon -> {
                        Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null, // Provide a description if needed
                            modifier = Modifier
                                .size(24.dp) // Adjust size as needed
                                .padding(start = 4.dp) // Space between the text and icon
                        )
                    }
                    is IconType.PainterIcon -> {
                        Icon(
                            painter = icon.painter,
                            contentDescription = null, // Provide a description if needed
                            modifier = Modifier
                                .size(24.dp) // Adjust size as needed
                                .padding(start = 4.dp) // Space between the text and icon
                        )
                    }
                }
            }
            if (image != null) {
                when (image) {
                    is IconType.ImageVectorIcon -> {
                        Image(
                            imageVector = image.imageVector,
                            contentDescription = null, // Provide a description if needed
                            modifier = Modifier
                                .size(24.dp) // Adjust size as needed
                                .padding(start = 4.dp) // Space between the text and icon
                        )
                    }
                    is IconType.PainterIcon -> {
                        Image(
                            painter = image.painter,
                            contentDescription = null, // Provide a description if needed
                            modifier = Modifier
                                .size(24.dp) // Adjust size as needed
                                .padding(start = 4.dp) // Space between the text and icon
                        )
                    }
                }
            }
        }
    }
}

sealed class IconType {
    data class ImageVectorIcon(val imageVector: ImageVector) : IconType()
    data class PainterIcon(val painter: Painter) : IconType()
}
