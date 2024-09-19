package com.android.banquetmanager.ui.screen

import android.app.DatePickerDialog
import android.graphics.Paint.Align
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.android.banquetmanager.utils.PaymentMode
import com.android.banquetmanager.utils.SlotTime
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

    var lunch by remember { mutableStateOf(slot == SlotTime.LUNCH.name) }
    var dinner by remember { mutableStateOf(slot == SlotTime.DINNER.name) }
    // State for dynamic payment details
    var paymentDetails by remember { mutableStateOf(listOf<Payment>()) }
    val context = LocalContext.current
    // Add vertical scroll
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "New Booking",
                        style = MaterialTheme.typography.titleLarge, // Adjust text style if needed
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
//                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {  },
                modifier = Modifier
                    .fillMaxWidth(),
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection) // Attach scroll behavior
                .padding(it)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            item {
                //Dropdown for the banquet locations
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Banquet Location: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DropdownMenu(
                            list = BanquetLocations.entries,
                            selectedItem = banquetLocation,
                            onItemSelected = { banquetLocation = it },
                            label = "",
                            displayName = { it.displayName }
                        )
                    }
                }

                // Cocktail toggle and input
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Cocktail: ")
                    }

                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(checked = cocktail, onCheckedChange = { cocktail = it })
                        if (cocktail) {
                            TextField(
                                value = cocktailAmount,
                                onValueChange = { cocktailAmount = it },
                                label = {  },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // DJ toggle and input
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "DJ: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(checked = dj, onCheckedChange = { dj = it }, modifier = Modifier.padding(top = 8.dp))
                    }
                }

                // Extra Plates input
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Extra Plates: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = extraPlate,
                            onValueChange = { extraPlate = it },
                            label = {  },
                            singleLine = true,
                            maxLines = 1,
                            shape = MaterialTheme.shapes.small,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Flower toggle and input
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Flowers: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(checked = flower, onCheckedChange = { flower = it }, modifier = Modifier.padding(top = 8.dp))
                        if (flower) {
                            OutlinedTextField(
                                value = flowerAmount,
                                onValueChange = { flowerAmount = it },
                                label = {  },
                                singleLine = true,
                                maxLines = 1,
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.small
                            )
                        }
                    }
                }

                // Function Type input
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Function Type: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DropdownMenu(
                            list = FunctionType.entries,
                            selectedItem = functionType,
                            onItemSelected = { functionType = it },
                            label = "",
                            displayName = { it.displayName }
                        )
                    }
                }

                // Food Type input
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Food Type: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DropdownMenu(
                            list = FoodType.entries,
                            selectedItem = foodType,
                            onItemSelected = { foodType = it },
                            label = "",
                            displayName = { it.displayName }
                        )
                    }
                }

                // Menu input
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Menu: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DropdownMenu(
                            list = Menu.entries,
                            selectedItem = menu,
                            onItemSelected = { menu = it },
                            label = "",
                            displayName = { it.displayName }
                        )
                    }
                }

                // Package Amount input
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Package Amount: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = packageAmount,
                            onValueChange = { packageAmount = it },
                            label = {  },
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.small
                        )
                    }
                }

                // Pax input
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Pax: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = pax,
                            onValueChange = { pax = it },
                            label = {  },
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.small
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Date Booked: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showDatePicker = true
                                }
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(16.dp)
                        ) {
                            Text(text = dateBooked, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

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

                // Lunch toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Lunch: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = lunch,
                            onCheckedChange = { lunch = it },
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                // Dinner toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Dinner: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = dinner,
                            onCheckedChange = { dinner = it },
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth()
                ){
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add payment", tint = Color.Blue)

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(Color.Blue)) {
                                        append("Add Payment Details")
                                    }
                                },
                                modifier = Modifier
                                    .clickable {
                                        // Add new empty payment detail
                                        paymentDetails = paymentDetails + Payment()
                                    }
                                    .padding(vertical = 8.dp)
                            )
                        }

                        // Display all payment details forms
                        paymentDetails.forEachIndexed { index, paymentDetail ->
                            PaymentDetailsForm(
                                paymentDetail = paymentDetail,
                                onUpdate = { updatedDetail ->
                                    paymentDetails = paymentDetails.toMutableList().apply {
                                        this[index] = updatedDetail
                                    }
                                },
                                onDelete = { updatedDetail ->
                                    paymentDetails = paymentDetails.toMutableList().apply {
                                        removeAt(index)
                                    }
                                }
                            )
                        }
                    }
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
    }
}

@Composable
fun PaymentDetailsForm(
    paymentDetail: Payment,
    onUpdate: (Payment) -> Unit,
    onDelete: (Payment) -> Unit
) {

    var paymentMode by remember { mutableStateOf(PaymentMode.CASH) }

    Card(
        modifier = Modifier.padding(5.dp)
    ){
        Column {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete payment details",
                tint = Color.Red,
                modifier = Modifier.clickable { onDelete(paymentDetail) }
            )
            DropdownMenu(
                list = PaymentMode.entries,
                selectedItem = paymentMode,
                onItemSelected = { paymentMode = it },
                label = "",
                displayName = { it.displayName }
            )
            TextField(
                value = paymentDetail.amount.toString(),
                onValueChange = {
                    it.toDoubleOrNull()?.let { newAmount ->
                        onUpdate(paymentDetail.copy(amount = newAmount))
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = paymentDetail.paymentDescription,
                onValueChange = {
                    onUpdate(paymentDetail.copy(paymentDescription = it))
                },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
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
        OutlinedTextField(
            value = displayName(selectedItem),  // Show selected enum name
            onValueChange = {},  // No manual input, so no changes here
            readOnly = true,  // Read-only to prevent keyboard input
            label = { Text(label) },  // Display the passed label
            singleLine = true,
            maxLines = 1,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            shape = MaterialTheme.shapes.small,
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 12.sp),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor() // Necessary to anchor the dropdown to the TextField
                .padding(vertical = 4.dp)
        )

        // The dropdown menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }  // Close when clicked outside
        ) {
            // Loop through all enum values and display them in the dropdown
            list.forEach { item ->
                DropdownMenuItem(
                    text = { Text(displayName(item), style = MaterialTheme.typography.bodyMedium) },
                    onClick = {
                        onItemSelected(item)  // Update the selected item
                        expanded = false  // Close the dropdown after selection
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}


