package com.android.banquetmanager.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.Calendar


@Composable
fun CalendarScreen() {
    var currentYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var currentMonth by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }
    var selectedDate by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    val context = LocalContext.current

    Column {
        MonthView(
            currentYear = currentYear,
            currentMonth = currentMonth,
            onDateSelected = { day, month, year ->
                selectedDate = Pair(day, month)
                Toast.makeText(context, "Selected date: $day/$month/$year", Toast.LENGTH_SHORT).show()
            },
            onMonthChanged = { newMonth, newYear ->
                currentMonth = newMonth
                currentYear = newYear
            }
        )

        selectedDate?.let {
            Text(
                text = "Selected Date: ${it.first}/${it.second}/$currentYear",
                modifier = Modifier.padding(16.dp)
            )
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
    var selectedDay by remember { mutableStateOf<Int?>(null) } // Track the selected day
    val daysInMonth = remember { getDaysInMonth(currentMonth, currentYear) }
    val calendar = Calendar.getInstance().apply {
        set(currentYear, currentMonth - 1, 1) // Set the calendar to the first day of the current month
    }
    val firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK) // Day of the week for the first day of the month

    // Get today's date
    val today = Calendar.getInstance()
    val todayDay = today.get(Calendar.DAY_OF_MONTH)
    val todayMonth = today.get(Calendar.MONTH) + 1
    val todayYear = today.get(Calendar.YEAR)

    // Month names for display
    val monthNames = arrayOf(
        "January", "February", "March",
        "April", "May", "June",
        "July", "August", "September",
        "October", "November", "December"
    )

    Column(
        modifier = Modifier.padding(top = 60.dp)
    ) {
        // Header Row with Month Navigation
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

        // Header Row for Days
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

        // Days Grid
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
                                ) // Apply background color conditionally
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

                            // Add a bullet or marker below the current day
                            if (isToday) {
                                Text(
                                    text = "•",
                                    textAlign = TextAlign.Center,
                                    color = Color.Red, // Customize the bullet color
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


private fun getDaysInMonth(month: Int, year: Int): Int {
    val calendar = Calendar.getInstance().apply {
        set(year, month - 1, 1) // Set to the first day of the month
    }
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH) // Get the maximum number of days in the month
}