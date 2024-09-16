package com.android.banquetmanager.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.model.Payment
import com.android.banquetmanager.data.viewmodel.BookingViewmodel
import com.android.banquetmanager.utils.BanquetLocations
import kotlinx.coroutines.launch

@Composable
fun AddEventBooking(date: String, slot: String, bookingViewmodel: BookingViewmodel = hiltViewModel()) {
    // State for form inputs
    var banquetLocation by remember { mutableStateOf(BanquetLocations.SK_EASTEND) }
    var cocktail by remember { mutableStateOf(false) }
    var cocktailAmount by remember { mutableStateOf("") }
    var dj by remember { mutableStateOf(false) }
    var djAmount by remember { mutableStateOf("") }
    var extraPlate by remember { mutableStateOf("") }
    var flower by remember { mutableStateOf(false) }
    var flowerAmount by remember { mutableStateOf("") }
    var foodType by remember { mutableStateOf("") }
    var functionType by remember { mutableStateOf("") }
    var menu by remember { mutableStateOf("") }
    var packageAmount by remember { mutableStateOf("") }
    var pax by remember { mutableStateOf("") }
    var dateBooked by remember { mutableStateOf("") }
    var lunch by remember { mutableStateOf(false) }
    var dinner by remember { mutableStateOf(false) }
    // State for dynamic payment details
    var paymentDetails by remember { mutableStateOf(listOf<Payment>()) }

    val context = LocalContext.current
    // Add vertical scroll
    val scrollState = rememberScrollState()

    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(scrollState)) {
        Text("Add Event", style = MaterialTheme.typography.headlineMedium)

        // Banquet Location Dropdown
        BanquetLocationDropdown(
            selectedLocation = banquetLocation,
            onLocationSelected = { banquetLocation = it }
        )
        // Text Fields and Switches for Event details
//        TextField(
//            value = banquetLocation,
//            onValueChange = { banquetLocation = it },
//            label = { Text("Banquet Location") },
//            modifier = Modifier.fillMaxWidth()
//        )

        // Cocktail toggle and input
        Text(text = "Include Cocktail?")
        Switch(checked = cocktail, onCheckedChange = { cocktail = it }, modifier = Modifier.padding(top = 8.dp))
        if (cocktail) {
            TextField(
                value = cocktailAmount,
                onValueChange = { cocktailAmount = it },
                label = { Text("Cocktail Amount") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // DJ toggle and input
        Text(text = "Include DJ?")
        Switch(checked = dj, onCheckedChange = { dj = it }, modifier = Modifier.padding(top = 8.dp))
        if (dj) {
            TextField(
                value = djAmount,
                onValueChange = { djAmount = it },
                label = { Text("DJ Amount") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Extra Plates input
        TextField(
            value = extraPlate,
            onValueChange = { extraPlate = it },
            label = { Text("Extra Plates") },
            modifier = Modifier.fillMaxWidth()
        )

        // Flower toggle and input
        Text(text = "Include Flowers?")
        Switch(checked = flower, onCheckedChange = { flower = it }, modifier = Modifier.padding(top = 8.dp))
        if (flower) {
            TextField(
                value = flowerAmount,
                onValueChange = { flowerAmount = it },
                label = { Text("Flower Amount") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Food Type input
        TextField(
            value = foodType,
            onValueChange = { foodType = it },
            label = { Text("Food Type") },
            modifier = Modifier.fillMaxWidth()
        )

        // Function Type input
        TextField(
            value = functionType,
            onValueChange = { functionType = it },
            label = { Text("Function Type") },
            modifier = Modifier.fillMaxWidth()
        )

        // Menu input
        TextField(
            value = menu,
            onValueChange = { menu = it },
            label = { Text("Menu") },
            modifier = Modifier.fillMaxWidth()
        )

        // Package Amount input
        TextField(
            value = packageAmount,
            onValueChange = { packageAmount = it },
            label = { Text("Package Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        // Pax input
        TextField(
            value = pax,
            onValueChange = { pax = it },
            label = { Text("Pax") },
            modifier = Modifier.fillMaxWidth()
        )

        // Date Booked input
        TextField(
            value = dateBooked,
            onValueChange = { dateBooked = it },
            label = { Text("Date Booked") },
            modifier = Modifier.fillMaxWidth()
        )

        // Lunch toggle
        Text(text = "Lunch Booking?")
        Switch(checked = lunch, onCheckedChange = { lunch = it }, modifier = Modifier.padding(top = 8.dp))

        // Dinner toggle
        Text(text = "Dinner Booking?")
        Switch(checked = dinner, onCheckedChange = { dinner = it }, modifier = Modifier.padding(top = 8.dp))

        Text(
            text = "Add Payment Details",
            modifier = Modifier
                .clickable {
                    // Add new empty payment detail
                    paymentDetails = paymentDetails + Payment()
                }
                .padding(vertical = 8.dp)
        )

        // Display all payment details forms
        paymentDetails.forEachIndexed { index, paymentDetail ->
            PaymentDetailsForm(
                paymentDetail = paymentDetail,
                onUpdate = { updatedDetail ->
                    paymentDetails = paymentDetails.toMutableList().apply {
                        this[index] = updatedDetail
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to submit the form
        Button(onClick = {
            Toast.makeText(context, "Adding the booking", Toast.LENGTH_SHORT).show()
            // Pass the user input to the ViewModel to add the event booking
            scope.launch {
                bookingViewmodel.addBooking(
                    Event(
                        banquetLocation = banquetLocation.name,
                        cocktail = cocktail,
                        cocktailAmount = cocktailAmount.toDoubleOrNull() ?: 0.0,
                        dj = dj,
                        djAmount = djAmount.toDoubleOrNull() ?: 0.0,
                        extraPlate = extraPlate.toLongOrNull() ?: 0,
                        flower = flower,
                        flowerAmount = flowerAmount.toDoubleOrNull() ?: 0.0,
                        foodType = foodType,
                        functionType = functionType,
                        menu = menu,
                        packageAmount = packageAmount.toDoubleOrNull() ?: 0.0,
                        pax = pax.toLongOrNull() ?: 0,
                        dateBooked = dateBooked,
                        lunch = lunch,
                        dinner = dinner
                    ),
                    paymentDetails
                )
            }
//            onEventAdded() // Callback when the event is added
        }) {
            Text("Add Event")
        }
    }
}

@Composable
fun PaymentDetailsForm(
    paymentDetail: Payment,
    onUpdate: (Payment) -> Unit
) {
    Column {
        TextField(
            value = paymentDetail.paymentMode,
            onValueChange = { onUpdate(paymentDetail.copy(paymentMode = it)) },
            label = { Text("Payment Type") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = paymentDetail.amount.toString(),
            onValueChange = {
                it.toDoubleOrNull()?.let { newAmount ->
                    onUpdate(paymentDetail.copy(amount = newAmount))
                }
            },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BanquetLocationDropdown(
    selectedLocation: BanquetLocations,
    onLocationSelected: (BanquetLocations) -> Unit
) {
    // State to manage the expanded dropdown
    var expanded by remember { mutableStateOf(false) }

    // UI for the dropdown
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }  // Toggle expanded state on click
    ) {
        // The TextField that displays the selected location and toggles the dropdown
        TextField(
            value = selectedLocation.name,  // Show selected location name
            onValueChange = {},  // No manual input, so no changes here
            readOnly = true,  // Read-only to prevent keyboard input
            label = { Text("Select Banquet Location") },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor() // Necessary to anchor the dropdown to the TextField
        )

        // The dropdown menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }  // Close when clicked outside
        ) {
            // Loop through all enum values and display them in the dropdown
            BanquetLocations.values().forEach { location ->
                Text(
                    text = location.name,  // Display the enum name
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onLocationSelected(location)  // Update the selected location
                            expanded = false  // Close the dropdown after selection
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}

