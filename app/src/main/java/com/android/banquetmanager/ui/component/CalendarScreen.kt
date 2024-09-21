package com.android.banquetmanager.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.viewmodel.BookingViewmodel
import com.android.banquetmanager.utils.BanquetLocations
import com.android.banquetmanager.utils.FoodType
import com.android.banquetmanager.utils.SlotTime
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(navController: NavController, viewModel: BookingViewmodel = hiltViewModel()) {
    var currentYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var currentMonth by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }
    var selectedDate by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var balanceEvents by remember { mutableStateOf<List<Event>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()

    // Main UI
    LaunchedEffect(selectedDate, currentMonth, currentYear) {
        if (selectedDate != null) {
            val date = "${String.format("%02d", selectedDate!!.first)}/${String.format("%02d", selectedDate!!.second)}/$currentYear"
            events = viewModel.getBookingsByDate(date)
            if (events.isNotEmpty()) {
                scope.launch {
                    sheetState.show()
                }
            }
        }
        val fetchedEvents = viewModel.getMonthlyBalancesByMonthYear(currentMonth, currentYear)
        // Update the state with the fetched events
        balanceEvents = fetchedEvents
    }

    Column {
        MonthView(
            currentYear = currentYear,
            currentMonth = currentMonth,
            onDateSelected = { day, month, year ->
                selectedDate = Pair(day, month)
                showBottomSheet = true
            },
            onMonthChanged = { newMonth, newYear ->
                currentMonth = newMonth
                currentYear = newYear
                selectedDate = null
                showBottomSheet = false
            }
        )

        BalancesView(navController, balanceEvents)
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    selectedDate = null
                    showBottomSheet = false
                }
            },
            sheetState = sheetState
        ){
            if (selectedDate != null) {
                val date = "${String.format("%02d", selectedDate!!.first)}/${String.format("%02d", selectedDate!!.second)}/$currentYear"
                BottomSheetContent(navController, date, events = events, onEventClick = {
                    // Navigate to details screen if needed
                    navController.navigate(Screen.DateDetailsScreen.createRoute(it!!.eventId))
                })
            }
        }
    }
}

@Composable
fun MonthView(
    currentYear: Int,
    currentMonth: Int,
    onDateSelected: (Int, Int, Int) -> Unit,
    onMonthChanged: (Int, Int) -> Unit
) {
    var selectedDay by remember { mutableStateOf<Int?>(null) }
    val daysInMonth = remember { getDaysInMonth(currentMonth, currentYear) }
    val calendar = Calendar.getInstance().apply {
        set(currentYear, currentMonth - 1, 1)
    }
    val firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK)
    val today = Calendar.getInstance()
    val todayDay = today.get(Calendar.DAY_OF_MONTH)
    val todayMonth = today.get(Calendar.MONTH) + 1
    val todayYear = today.get(Calendar.YEAR)
    val monthNames = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(top = 60.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                val newMonth = if (currentMonth == 1) 12 else currentMonth - 1
                val newYear = if (currentMonth == 1) currentYear - 1 else currentYear
                onMonthChanged(newMonth, newYear)
            }) {
                Text(text = "<")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "${monthNames[currentMonth - 1]} $currentYear",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                val newMonth = if (currentMonth == 12) 1 else currentMonth + 1
                val newYear = if (currentMonth == 12) currentYear + 1 else currentYear
                onMonthChanged(newMonth, newYear)
            }) {
                Text(text = ">")
            }
        }

        Row {
            for (dayOfWeek in arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")) {
                Text(
                    text = dayOfWeek,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        var day = 1
        for (row in 0 until 6) {
            Row {
                for (col in 0 until 7) {
                    val isEmpty = row == 0 && col < (firstDayOfMonth - 1) || day > daysInMonth
                    if (isEmpty) {
                        Text(
                            text = "",
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        val dayToDisplay = day
                        val isToday =
                            dayToDisplay == todayDay && currentMonth == todayMonth && currentYear == todayYear

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                                .height(30.dp)
                                .clickable {
                                    selectedDay = dayToDisplay
                                    onDateSelected(dayToDisplay, currentMonth, currentYear)
                                },
                            contentAlignment = Alignment.Center // Align content centrally in the Box
                        ) {
                            // Draw a circle around the date if it's today
                            if (isToday) {
                                Canvas(modifier = Modifier.size(40.dp)) {
                                    drawCircle(
                                        color = Color.Red,
                                        style = Stroke(width = 1.dp.toPx()) // Stroke width for the circle outline
                                    )
                                }
                            }

                            // Display the day inside the box
                            Text(
                                text = dayToDisplay.toString(),
                                textAlign = TextAlign.Center
                            )
                        }
                        day++
                    }
                }
            }
        }
    }
}


@Composable
fun BottomSheetContent(
    navController: NavController,
    date: String,
    events: List<Event>,
    onEventClick: (Event?) -> Unit
) {
    // Group events by banquet location
    val groupedEvents = events.groupBy { it.banquetLocation }
    Text(text = date)
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Iterate over the list of all banquet locations
        BanquetLocations.entries.forEach { banquetLocation ->
            // Get the events for the current banquet location
            val eventsForLocation = groupedEvents[banquetLocation.name] ?: emptyList()

            // Determine slot availability for lunch and dinner
            val lunchEvent = eventsForLocation.firstOrNull { !it.lunch }
            val dinnerEvent = eventsForLocation.firstOrNull { !it.dinner }

            val isLunchBooked = lunchEvent != null
            val isDinnerBooked = dinnerEvent != null

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Location name
                Text(
                    text = banquetLocation.displayName,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )

                // Display lunch and dinner slots
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // Lunch Slot
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (!isLunchBooked) {
                                    // Handle creating a new lunch booking
                                    navController.navigate(
                                        Screen.AddEventBooking.createRoute(
                                            date,
                                            SlotTime.LUNCH.name
                                        )
                                    )// No event means it's available for booking
                                } else {
                                    // Handle existing booking if necessary
                                    lunchEvent?.let { onEventClick(it) } // Pass the booked event
                                }
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "Lunch",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Row {
                            Text(
                                text = if (isLunchBooked) "Booked" else "Available",
                                color = if (isLunchBooked) Color.Red else Color.Green
                            )
                            // Display food type if lunch is booked
                            if (isLunchBooked) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "(${lunchEvent?.foodType?.capitalize()})", // Show Veg/Non-Veg
                                    color = if (lunchEvent?.foodType == FoodType.VEG.name) Color.Green else Color.Red,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Dinner Slot
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (!isDinnerBooked) {
                                    // Handle creating a new dinner booking
                                    navController.navigate(
                                        Screen.AddEventBooking.createRoute(
                                            date,
                                            SlotTime.DINNER.name
                                        )
                                    )// No event means it's available for booking
                                } else {
                                    // Handle existing booking if necessary
                                    dinnerEvent?.let { onEventClick(it) } // Pass the booked event
                                }
                            }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Dinner",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Row {
                            Text(
                                text = if (isDinnerBooked) "Booked" else "Available",
                                color = if (isDinnerBooked) Color.Red else Color.Green
                            )
                            // Display food type if dinner is booked
                            if (isDinnerBooked) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "(${dinnerEvent?.foodType?.capitalize()})", // Show Veg/Non-Veg
                                    color = if (dinnerEvent?.foodType == FoodType.VEG.name) Color.Green else Color.Red,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BalancesView(
    navController: NavController,
    balanceEvents: List<Event>
) {

    // Display a list of event balance cards
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(balanceEvents) { event ->
            BalanceCard(event = event)
        }
    }
}

@Composable
fun BalanceCard(event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Event: ${event.functionType}"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Date Booked: ${event.dateBooked}"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Balance: ${event.balance}",
                color = if (event.balance > 0) Color.Red else Color.Red
            )
        }
    }
}


private fun getDaysInMonth(month: Int, year: Int): Int {
    val calendar = Calendar.getInstance().apply {
        set(year, month - 1, 1)
    }
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}
