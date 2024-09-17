package com.android.banquetmanager.ui.screen

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.model.Payment
import com.android.banquetmanager.data.viewmodel.BookingViewmodel
import com.android.banquetmanager.utils.BanquetLocations
import com.android.banquetmanager.utils.FoodType
import com.android.banquetmanager.utils.FunctionType
import com.android.banquetmanager.utils.Menu
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.enums.EnumEntries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventBooking(date: String, slot: String, bookingViewmodel: BookingViewmodel = hiltViewModel()) {
    // State for form inputs
    var banquetLocation by remember { mutableStateOf(BanquetLocations.SK_EASTEND) }
    var functionType by remember { mutableStateOf(FunctionType.WEDDING) }
    var foodType by remember { mutableStateOf(FoodType.VEG) }
    var menu by remember { mutableStateOf(Menu.Gold) }
    var cocktail by remember { mutableStateOf(false) }
    var cocktailAmount by remember { mutableStateOf("") }
    var dj by remember { mutableStateOf(false) }
    var djAmount by remember { mutableStateOf("") }
    var extraPlate by remember { mutableStateOf("") }
    var flower by remember { mutableStateOf(false) }
    var flowerAmount by remember { mutableStateOf("") }
    var packageAmount by remember { mutableStateOf("") }
    var pax by remember { mutableStateOf("") }

    // Updated state for date picker
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var dateBooked by remember { mutableStateOf(date) }
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

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

        //Dropdown for the banquet locations
        DropdownMenu(
            list = BanquetLocations.entries,
            selectedItem = banquetLocation,
            onItemSelected = { banquetLocation = it },
            label = "Banquet Location",
            displayName = { it.displayName }
            )

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

        // Function Type input
        DropdownMenu(
            list = FunctionType.entries,
            selectedItem = functionType,
            onItemSelected = { functionType = it },
            label = "Function type",
            displayName = { it.displayName }
        )

        // Food Type input
        DropdownMenu(
            list = FoodType.entries,
            selectedItem = foodType,
            onItemSelected = { foodType = it },
            label = "Food type",
            displayName = { it.displayName }
        )

        // Menu input
        DropdownMenu(
            list = Menu.entries,
            selectedItem = menu,
            onItemSelected = { menu = it },
            label = "Menu",
            displayName = { it.displayName }
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

        // Date picker for "Date Booked"
        if(showDatePicker){
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        dateBooked = selectedDate
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showDatePicker = true
                }
                .border(1.dp, MaterialTheme.colorScheme.outline)
                .padding(16.dp)
        ) {
            Text(text = dateBooked, style = MaterialTheme.typography.bodyLarge)
        }

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
                        foodType = foodType.name,
                        functionType = functionType.name,
                        menu = menu.name,
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
fun <T : Enum<T>> DropdownMenu(
    list: EnumEntries<T>,  // List of enum values
    selectedItem: T,  // Currently selected enum item
    onItemSelected: (T) -> Unit,  // Callback when an item is selected
    label: String,  // Label for the dropdown field
    displayName: (T) -> String
) {
    // State to manage the expanded dropdown
    var expanded by remember { mutableStateOf(false) }

    // UI for the dropdown
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }  // Toggle expanded state on click
    ) {
        // The TextField that displays the selected enum and toggles the dropdown
        TextField(
            value = displayName(selectedItem),  // Show selected enum name
            onValueChange = {},  // No manual input, so no changes here
            readOnly = true,  // Read-only to prevent keyboard input
            label = { Text(label) },  // Display the passed label
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
            list.forEach { item ->
                Text(
                    text = displayName(item),  // Display the enum name
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onItemSelected(item)  // Update the selected item
                            expanded = false  // Close the dropdown after selection
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}


