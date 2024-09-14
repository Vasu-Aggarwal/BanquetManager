package com.android.banquetmanager.ui.component

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.viewmodel.BookingViewmodel
import com.android.banquetmanager.ui.screen.Screen
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
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()

    // Main UI
    LaunchedEffect(selectedDate) {
        if (selectedDate != null) {
            val date = "${String.format("%02d", selectedDate!!.first)}/${String.format("%02d", selectedDate!!.second)}/$currentYear"
            events = viewModel.getBookingsByDate(date)
            if (events.isNotEmpty()) {
                scope.launch {
                    sheetState.show()
                }
            }
        }
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
                showBottomSheet = false
            }
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    showBottomSheet = false
                }
            },
            sheetState = sheetState
        ){
            BottomSheetContent(events = events, onEventClick = {
                // Navigate to details screen if needed
                navController.navigate(Screen.DateDetailsScreen.createRoute(it.eventId))
             }, onDismiss = {
                scope.launch {
                    sheetState.hide()
                    showBottomSheet = false
                }
            })
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

    Column(
        modifier = Modifier.padding(top = 60.dp)
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

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                                .background(
                                    if (selectedDay == dayToDisplay) Color.Blue else Color.Transparent
                                )
                                .height(50.dp)
                                .clickable {
                                    selectedDay = dayToDisplay
                                    onDateSelected(dayToDisplay, currentMonth, currentYear)
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = dayToDisplay.toString(),
                                textAlign = TextAlign.Center
                            )

                            if (isToday) {
                                Text(
                                    text = "•",
                                    textAlign = TextAlign.Center,
                                    color = Color.Red,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                        day++
                    }
                }
            }
        }
    }
}

@Composable
fun BottomSheetContent(events: List<Event>, onDismiss: () -> Unit, onEventClick: (Event) -> Unit) {
    if (events.isNotEmpty()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            events.forEach { event ->
                // Create a clickable Row or Column for each event
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEventClick(event) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = event.banquetLocation,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = event.functionType,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    } else {
        Text(
            text = "No events found",
            modifier = Modifier.padding(16.dp)
        )
    }
}


private fun getDaysInMonth(month: Int, year: Int): Int {
    val calendar = Calendar.getInstance().apply {
        set(year, month - 1, 1)
    }
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}
