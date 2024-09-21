package com.android.banquetmanager.ui.screen

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.model.Payment
import com.android.banquetmanager.data.viewmodel.BookingViewmodel
import com.android.banquetmanager.utils.BanquetLocations
import com.android.banquetmanager.utils.FoodType
import com.android.banquetmanager.utils.FunctionType
import com.android.banquetmanager.utils.Menu
import com.android.banquetmanager.utils.PaymentMode
import com.android.banquetmanager.utils.SlotTime
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.enums.EnumEntries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventBooking(date: String, slot: String, navController: NavController, bookingViewmodel: BookingViewmodel = hiltViewModel()) {
    // State for form inputs
    var banquetLocation by remember { mutableStateOf(BanquetLocations.SK_EASTEND) }
    var functionType by remember { mutableStateOf(FunctionType.WEDDING) }
    var foodType by remember { mutableStateOf(FoodType.VEG) }
    var menu by remember { mutableStateOf(Menu.Gold) }
    var cocktail by remember { mutableStateOf(false) }
    var cocktailAmount by remember { mutableStateOf("") }
    var displayCocktailAmount by remember { mutableStateOf("") }
    var dj by remember { mutableStateOf(false) }
    var flower by remember { mutableStateOf(false) }
    var flowerAmount by remember { mutableStateOf("") }
    var displayFlowerAmount by remember { mutableStateOf("") }
    var packageAmount by remember { mutableStateOf("") }
    var displayPackageAmount by remember { mutableStateOf("") }
    var pax by remember { mutableStateOf("") }
    var guestName by remember { mutableStateOf("") }
    var guestMobile by remember { mutableStateOf("") }
    var guestEmail by remember { mutableStateOf("") }
    var fruit by remember { mutableStateOf(false) }
    var fruitAmount by remember { mutableStateOf("") }
    var displayFruitAmount by remember { mutableStateOf("") }
    var threeSixty by remember { mutableStateOf(false) }
    var threeSixtyAmount by remember { mutableStateOf("") }
    var displayThreeSixtyAmount by remember { mutableStateOf("") }
    var extraEvent by remember { mutableStateOf(false) }
    var extraEventAmount by remember { mutableStateOf("") }
    var displayExtraEventAmount by remember { mutableStateOf("") }

    // Updated state for date picker
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var dateBooked by remember { mutableStateOf(date) }
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    // Convert selectedDateMillis to a Firestore Timestamp
    val selectedTimestamp = datePickerState.selectedDateMillis?.let {
        Timestamp(Date(it)) // Firestore Timestamp from milliseconds
    }

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
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {  },
                modifier = Modifier
                    .fillMaxWidth(),
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            // Floating Action Button at the bottom right
            FloatingActionButton(
                onClick = {
                    // Add new empty payment detail
                    paymentDetails = paymentDetails + Payment()
                },
                modifier = Modifier
                    .padding(16.dp), // Add padding to keep FAB away from the edge of the screen
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add payment")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection) // Attach scroll behavior
                .padding(it)
                .padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            item {

                //1. Function Type input
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

                // 2. Guest name input
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Guest Name: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = guestName,
                            onValueChange = {
                                guestName = it
                            },
                            label = {  },
                            singleLine = true,
                            maxLines = 1,
//                            leadingIcon = { Icon(imageVector = Icons.Default.CurrencyRupee, contentDescription = "Rupee") },
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.small
                        )
                    }
                }

                // 3. Guest mobile input
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Guest Mobile: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = guestMobile,
                            onValueChange = {
                                guestMobile = it
                            },
                            label = {  },
                            singleLine = true,
                            maxLines = 1,
//                            leadingIcon = { Icon(imageVector = Icons.Default.CurrencyRupee, contentDescription = "Rupee") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.small
                        )
                    }
                }

                // 4. Guest Email input
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Guest Email: ")
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = guestEmail,
                            onValueChange = {
                                guestEmail = it
                            },
                            label = {  },
                            singleLine = true,
                            maxLines = 1,
//                            leadingIcon = { Icon(imageVector = Icons.Default.CurrencyRupee, contentDescription = "Rupee") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.small
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
                        Checkbox(checked = lunch, onCheckedChange = { lunch = it })
//                        Switch(
//                            enabled = false,
//                            checked = lunch,
//                            onCheckedChange = { lunch = it },
//                            modifier = Modifier.padding(top = 8.dp)
//                        )
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
                        Checkbox(checked = dinner, onCheckedChange = { dinner = it })
//                        Switch(
//                            checked = dinner,
//                            onCheckedChange = { dinner = it },
//                            modifier = Modifier.padding(top = 8.dp)
//                        )
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

                // Flower toggle and input
                Column(modifier = Modifier.fillMaxWidth()) {
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
                            Checkbox(checked = flower, onCheckedChange = { flower = it })
//                            Switch(checked = flower, onCheckedChange = { flower = it }, modifier = Modifier.padding(top = 8.dp))
                        }
                    }

                    if (flower) {
                        OutlinedTextField(
                            value = displayFlowerAmount,
                            onValueChange = {
                                flowerAmount = it.replace(",", "").replace("₹", "")
                                // Convert the cleaned string to a number for validation
                                val parsedAmount = flowerAmount.toLongOrNull()

                                // Only format if the input is a valid number
                                if (parsedAmount != null) {
                                    // Format the number with commas for display
                                    val numberFormat = NumberFormat.getInstance(Locale("en", "IN"))
                                    displayFlowerAmount = numberFormat.format(parsedAmount) // Format for display
                                } else {
                                    displayFlowerAmount = it // If not a valid number, just show the input
                                }
                            },
                            label = { Text(text = "Flower Amount") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            maxLines = 1,
                            leadingIcon = { Icon(imageVector = Icons.Default.CurrencyRupee, contentDescription = "Rupee") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.small
                        )
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
                        Checkbox(checked = dj, onCheckedChange = { dj = it })
//                        Switch(checked = dj, onCheckedChange = { dj = it }, modifier = Modifier.padding(top = 8.dp))
                    }
                }

                // Cocktail toggle and input
                Column(modifier = Modifier.fillMaxWidth()) {
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
                            Checkbox(checked = cocktail, onCheckedChange = { cocktail = it })
//                            Switch(checked = cocktail, onCheckedChange = { cocktail = it })
                        }
                    }

                    // Second Row: Display OutlinedTextField only if cocktail is true
                    if (cocktail) {
                        OutlinedTextField(
                            value = displayCocktailAmount,
                            onValueChange = {
                                cocktailAmount = it.replace(",", "").replace("₹", "")
                                // Convert the cleaned string to a number for validation
                                val parsedAmount = cocktailAmount.toLongOrNull()

                                // Only format if the input is a valid number
                                if (parsedAmount != null) {
                                    // Format the number with commas for display
                                    val numberFormat = NumberFormat.getInstance(Locale("en", "IN"))
                                    displayCocktailAmount = numberFormat.format(parsedAmount) // Format for display
                                } else {
                                    displayCocktailAmount = it // If not a valid number, just show the input
                                }
                            },
                            label = { Text(text = "Cocktail Amount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = { Icon(imageVector = Icons.Default.CurrencyRupee, contentDescription = "Rupee") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                //Fruit
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Fruit: ")
                        }

                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = fruit, onCheckedChange = { fruit = it })
//                            Switch(checked = cocktail, onCheckedChange = { cocktail = it })
                        }
                    }

                    // Second Row: Display OutlinedTextField only if cocktail is true
                    if (fruit) {
                        OutlinedTextField(
                            value = displayFruitAmount,
                            onValueChange = {
                                fruitAmount = it.replace(",", "").replace("₹", "")
                                // Convert the cleaned string to a number for validation
                                val parsedAmount = fruitAmount.toLongOrNull()

                                // Only format if the input is a valid number
                                if (parsedAmount != null) {
                                    // Format the number with commas for display
                                    val numberFormat = NumberFormat.getInstance(Locale("en", "IN"))
                                    displayFruitAmount = numberFormat.format(parsedAmount) // Format for display
                                } else {
                                    displayFruitAmount = it // If not a valid number, just show the input
                                }
                            },
                            label = { Text(text = "Fruit Amount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = { Icon(imageVector = Icons.Default.CurrencyRupee, contentDescription = "Rupee") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                //360
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "360 Camera: ")
                        }

                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = threeSixty, onCheckedChange = { threeSixty = it })
//                            Switch(checked = cocktail, onCheckedChange = { cocktail = it })
                        }
                    }

                    // Second Row: Display OutlinedTextField only if cocktail is true
                    if (threeSixty) {
                        OutlinedTextField(
                            value = displayThreeSixtyAmount,
                            onValueChange = {
                                threeSixtyAmount = it.replace(",", "").replace("₹", "")
                                // Convert the cleaned string to a number for validation
                                val parsedAmount = threeSixtyAmount.toLongOrNull()

                                // Only format if the input is a valid number
                                if (parsedAmount != null) {
                                    // Format the number with commas for display
                                    val numberFormat = NumberFormat.getInstance(Locale("en", "IN"))
                                    displayThreeSixtyAmount = numberFormat.format(parsedAmount) // Format for display
                                } else {
                                    displayThreeSixtyAmount = it // If not a valid number, just show the input
                                }
                            },
                            label = { Text(text = "360 Amount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = { Icon(imageVector = Icons.Default.CurrencyRupee, contentDescription = "Rupee") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                //Extra event
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Extra Event: ")
                        }

                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = extraEvent, onCheckedChange = { extraEvent = it })
//                            Switch(checked = cocktail, onCheckedChange = { cocktail = it })
                        }
                    }

                    // Second Row: Display OutlinedTextField only if cocktail is true
                    if (extraEvent) {
                        OutlinedTextField(
                            value = displayExtraEventAmount,
                            onValueChange = {
                                extraEventAmount = it.replace(",", "").replace("₹", "")
                                // Convert the cleaned string to a number for validation
                                val parsedAmount = extraEventAmount.toLongOrNull()

                                // Only format if the input is a valid number
                                if (parsedAmount != null) {
                                    // Format the number with commas for display
                                    val numberFormat = NumberFormat.getInstance(Locale("en", "IN"))
                                    displayExtraEventAmount = numberFormat.format(parsedAmount) // Format for display
                                } else {
                                    displayExtraEventAmount = it // If not a valid number, just show the input
                                }
                            },
                            label = { Text(text = "Extra event Amount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = { Icon(imageVector = Icons.Default.CurrencyRupee, contentDescription = "Rupee") },
                            modifier = Modifier.fillMaxWidth()
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
                            value = displayPackageAmount,
                            onValueChange = {
                                packageAmount = it.replace(",", "").replace("₹", "")
                                // Convert the cleaned string to a number for validation
                                val parsedAmount = packageAmount.toLongOrNull()

                                // Only format if the input is a valid number
                                if (parsedAmount != null) {
                                    // Format the number with commas for display
                                    val numberFormat = NumberFormat.getInstance(Locale("en", "IN"))
                                    displayPackageAmount = numberFormat.format(parsedAmount) // Format for display
                                } else {
                                    displayPackageAmount = it // If not a valid number, just show the input
                                }
                            },
                            label = {  },
                            singleLine = true,
                            maxLines = 1,
                            leadingIcon = { Icon(imageVector = Icons.Default.CurrencyRupee, contentDescription = "Rupee") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.small
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Date to be Booked: ")
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
                                .padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = dateBooked, style = MaterialTheme.typography.bodyLarge)
                                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Date picker")
                            }
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // Display all payment details forms
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        paymentDetails.forEachIndexed { index, paymentDetail ->
                            PaymentDetailsForm(
                                paymentDetail = paymentDetail,
                                onUpdate = { updatedDetail ->
                                    paymentDetails = paymentDetails.toMutableList().apply {
                                        this[index] = updatedDetail
                                    }
                                },
                                onDelete = {
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
//                                extraPlate = extraPlate.toLongOrNull() ?: 0,
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Main card with payment details
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp), // Add some padding so the delete button is not overlapped
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dropdown for Payment Mode
                DropdownMenu(
                    list = PaymentMode.entries,
                    selectedItem = paymentMode,
                    onItemSelected = { paymentMode = it },
                    label = "",
                    displayName = { it.displayName }
                )
                // Amount input field
                OutlinedTextField(
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
                // Description input field
                OutlinedTextField(
                    value = paymentDetail.paymentDescription,
                    onValueChange = {
                        onUpdate(paymentDetail.copy(paymentDescription = it))
                    },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    modifier = Modifier
                        .width(130.dp)
                        .padding(top = 10.dp),
                    onClick = { onDelete(paymentDetail) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "DELETE")
                }
            }
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


