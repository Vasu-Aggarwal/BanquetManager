package com.android.banquetmanager.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.FilterAltOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.viewmodel.BookingViewmodel
import com.android.banquetmanager.utils.BanquetLocations
import com.android.banquetmanager.utils.FoodType
import com.android.banquetmanager.utils.FunctionType
import com.android.banquetmanager.utils.Menu

@Composable
fun FilterScreen(
    viewModel: BookingViewmodel = hiltViewModel(),
    navController: NavController
) {
    var selectedBanquetLocations by remember { mutableStateOf(emptyList<String>()) }
    var cocktailFilter by remember { mutableStateOf(false) }
    var flowerFilter by remember { mutableStateOf(false) }
    var djFilter by remember { mutableStateOf(false) }
    var selectedFoodType by remember { mutableStateOf(emptyList<String>()) }
    var selectedFunctionType by remember { mutableStateOf(emptyList<String>()) }
    var selectedMenu by remember { mutableStateOf(emptyList<String>()) }
    var selectedStatus by remember { mutableStateOf(emptyList<String>()) }
    var lunchFilter by remember { mutableStateOf(false) }
    var dinnerFilter by remember { mutableStateOf(false) }
    var balanceFilter by remember { mutableStateOf<Long?>(null) }

    var events by remember { mutableStateOf<List<Event>>(emptyList()) }

    LaunchedEffect(Unit) {
        val fetchedEvents = viewModel.getEventsByMonthAndYear(9, 2024)
        // Update the state with the fetched events
        events = fetchedEvents
    }

    LazyColumn {
        item {
            FilterSection(
                selectedBanquetLocations = selectedBanquetLocations,
                onBanquetLocationSelected = { updatedLocations ->
                    selectedBanquetLocations = updatedLocations
                },
                cocktailFilter = cocktailFilter,
                onCocktailFilterChanged = { cocktailFilter = it },
                flowerFilter = flowerFilter,
                onFlowerFilterChanged = { flowerFilter = it },
                djFilter = djFilter,
                onDjFilterChanged = { djFilter = it },
                lunchFilter = lunchFilter,
                onLunchFilterChanged = { lunchFilter = it },
                dinnerFilter = dinnerFilter,
                onDinnerFilterChanged = { dinnerFilter = it },
                balanceFilter = balanceFilter,
                onBalanceFilterChanged = { balanceFilter = it },
                selectedFunctionType = selectedFunctionType,
                onFunctionTypeSelected = { updatedFunctions ->
                    selectedFunctionType = updatedFunctions
                },
                selectedFoodType = selectedFoodType,
                onFoodTypeSelected = { updatedFood ->
                    selectedFoodType = updatedFood
                },
                selectedMenuType = selectedMenu,
                onMenuTypeSelected = { updatedMenu ->
                    selectedMenu = updatedMenu
                }
            )
        }

        items(events.filter { event ->
            (selectedBanquetLocations.isEmpty() || selectedBanquetLocations.contains(event.banquetLocation)) &&
                    (!cocktailFilter || event.cocktail) &&
                    (!flowerFilter || event.flower) &&
                    (!djFilter || event.dj) &&
                    (selectedFoodType.isEmpty() || selectedFoodType.contains(event.foodType)) &&
                    (selectedFunctionType.isEmpty() || selectedFunctionType.contains(event.functionType)) &&
                    (selectedMenu.isEmpty() || selectedMenu.contains(event.menu)) &&
                    (!lunchFilter || event.lunch) &&
                    (!dinnerFilter || event.dinner) &&
                    (balanceFilter == null || event.balance == balanceFilter)
        }) { event ->
            EventItem(event = event, navController = navController)
        }
    }
}


@Composable
fun FilterSection(
    selectedBanquetLocations: List<String>,  // To store selected locations
    onBanquetLocationSelected: (List<String>) -> Unit,  // Callback when a location is selected/unselected
    selectedFoodType: List<String>,
    onFoodTypeSelected: (List<String>) -> Unit,
    selectedFunctionType: List<String>,
    onFunctionTypeSelected: (List<String>) -> Unit,
    selectedMenuType: List<String>,
    onMenuTypeSelected: (List<String>) -> Unit,
//    selectedStatus: List<String>,
//    onStatusSelected: (List<String>) -> Unit,
    cocktailFilter: Boolean,
    onCocktailFilterChanged: (Boolean) -> Unit,
    flowerFilter: Boolean,
    onFlowerFilterChanged: (Boolean) -> Unit,
    djFilter: Boolean,
    onDjFilterChanged: (Boolean) -> Unit,
    lunchFilter: Boolean,
    onLunchFilterChanged: (Boolean) -> Unit,
    dinnerFilter: Boolean,
    onDinnerFilterChanged: (Boolean) -> Unit,
    balanceFilter: Long?,
    onBalanceFilterChanged: (Long?) -> Unit
) {
    var isFilterVisible by remember { mutableStateOf(false) } // Control visibility of the filter section

    Column(Modifier.padding(16.dp)) {
        // Toggle button to show/hide the filter section
        Button(onClick = { isFilterVisible = !isFilterVisible }) {
            if (isFilterVisible)
                Icon(imageVector = Icons.Default.FilterAlt, contentDescription = "Hide filters")
            else
                Icon(imageVector = Icons.Outlined.FilterAltOff, contentDescription = "Show filters")
        }

        // Filter section visibility controlled by isFilterVisible
        if (isFilterVisible) {
            Spacer(modifier = Modifier.height(16.dp)) // Optional space between button and filter section

            // Banquet Location Checkboxes (dynamically generated)
            Text(text = "Banquet Locations:")
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Adjust spacing as needed
            ) {
                BanquetLocations.entries.forEach { location ->
                    val isChecked = selectedBanquetLocations.contains(location.name)
                    CheckboxRow(
                        label = location.displayName,  // Using displayName if available
                        checked = isChecked,
                        onCheckedChange = { isSelected ->
                            val updatedSelections = if (isSelected) {
                                selectedBanquetLocations + location.name
                            } else {
                                selectedBanquetLocations - location.name
                            }
                            onBanquetLocationSelected(updatedSelections)
                        }
                    )
                }
            }

            // Food Type Checkboxes (dynamically generated)
            Text(text = "Food Type:")
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Adjust spacing as needed
            ) {
                FoodType.entries.forEach { type ->
                    val isChecked = selectedFoodType.contains(type.name)
                    CheckboxRow(
                        label = type.displayName,  // Using displayName if available
                        checked = isChecked,
                        onCheckedChange = { isSelected ->
                            val updatedSelections = if (isSelected) {
                                selectedFoodType + type.name
                            } else {
                                selectedFoodType - type.name
                            }
                            onFoodTypeSelected(updatedSelections)
                        }
                    )
                }
            }

            // Function Type Checkboxes (dynamically generated)
            Text(text = "Function Type:")
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Adjust spacing as needed
            ) {
                FunctionType.entries.forEach { type ->
                    val isChecked = selectedFunctionType.contains(type.name)
                    CheckboxRow(
                        label = type.displayName,  // Using displayName if available
                        checked = isChecked,
                        onCheckedChange = { isSelected ->
                            val updatedSelections = if (isSelected) {
                                selectedFunctionType + type.name
                            } else {
                                selectedFunctionType - type.name
                            }
                            onFunctionTypeSelected(updatedSelections)
                        }
                    )
                }
            }

            // Menu Type Checkboxes (dynamically generated)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Adjust spacing as needed
            ) {
                Menu.entries.forEach { type ->
                    val isChecked = selectedMenuType.contains(type.name)
                    CheckboxRow(
                        label = type.displayName,  // Using displayName if available
                        checked = isChecked,
                        onCheckedChange = { isSelected ->
                            val updatedSelections = if (isSelected) {
                                selectedMenuType + type.name
                            } else {
                                selectedMenuType - type.name
                            }
                            onMenuTypeSelected(updatedSelections)
                        }
                    )
                }
            }

            //status is left to be applied

            Text(text = "Other:")
            Column {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Adjust spacing as needed
                ) {
                    // Cocktail Filter
                    CheckboxRow(
                        label = "Cocktail",
                        checked = cocktailFilter,
                        onCheckedChange = onCocktailFilterChanged
                    )

                    // Flower Filter
                    CheckboxRow(
                        label = "Flower",
                        checked = flowerFilter,
                        onCheckedChange = onFlowerFilterChanged
                    )
                }
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Adjust spacing as needed
                ) {
                    // DJ Filter
                    CheckboxRow(
                        label = "DJ",
                        checked = djFilter,
                        onCheckedChange = onDjFilterChanged
                    )

                    // Lunch Filter
                    CheckboxRow(
                        label = "Lunch",
                        checked = lunchFilter,
                        onCheckedChange = onLunchFilterChanged
                    )
                }
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Adjust spacing as needed
                ) {
                    // Dinner Filter
                    CheckboxRow(
                        label = "Dinner",
                        checked = dinnerFilter,
                        onCheckedChange = onDinnerFilterChanged
                    )
                }
            }
        }
    }
}

@Composable
fun EventItem(event: Event, navController: NavController) {
    // UI for individual event
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("EventDetail/${event.eventId}")
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Event ID: ${event.eventId}")
            Text("Banquet Location: ${event.banquetLocation}")
            // Add more event details here
        }
    }
}

@Composable
fun CheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onCheckedChange(!checked) } // Click the whole row to toggle the checkbox
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(8.dp)) // Spacing between checkbox and label
        Text(text = label)
    }
}


// Reusable Components for DropdownMenu, CheckboxRow, and BalanceInput
