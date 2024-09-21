package com.android.banquetmanager.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
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

@Composable
fun FilterScreen(
    viewModel: BookingViewmodel = hiltViewModel(),
    navController: NavController
) {
    var selectedBanquetLocation by remember { mutableStateOf<String?>(null) }
    var cocktailFilter by remember { mutableStateOf(false) }
    var flowerFilter by remember { mutableStateOf(false) }
    var djFilter by remember { mutableStateOf(false) }
    var selectedFoodType by remember { mutableStateOf<String?>(null) }
    var selectedFunctionType by remember { mutableStateOf<String?>(null) }
    var selectedMenu by remember { mutableStateOf<String?>(null) }
    var selectedStatus by remember { mutableStateOf<Long?>(null) }
    var lunchFilter by remember { mutableStateOf(false) }
    var dinnerFilter by remember { mutableStateOf(false) }
    var balanceFilter by remember { mutableStateOf<Long?>(null) }

    val banquetLocations = listOf("SK_EASTEND", "RAJWADA")
    val foodTypes = listOf("Veg", "Non-Veg", "Mixed")
    val functionTypes = listOf("Wedding", "Reception", "Corporate", "Birthday")
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }

    LaunchedEffect(Unit) {
        val fetchedEvents = viewModel.getEventsByMonthAndYear(9, 2024)
        // Update the state with the fetched events
        events = fetchedEvents
    }

    Column {
        // Filters UI
        FilterSection(
            banquetLocations = banquetLocations,
            foodTypes = foodTypes,
            functionTypes = functionTypes,
            selectedBanquetLocation = selectedBanquetLocation,
            onBanquetLocationSelected = { selectedBanquetLocation = it },
            cocktailFilter = cocktailFilter,
            onCocktailFilterChanged = { cocktailFilter = it },
            flowerFilter = flowerFilter,
            onFlowerFilterChanged = { flowerFilter = it },
            djFilter = djFilter,
            onDjFilterChanged = { djFilter = it },
            selectedFoodType = selectedFoodType,
            onFoodTypeSelected = { selectedFoodType = it },
            selectedFunctionType = selectedFunctionType,
            onFunctionTypeSelected = { selectedFunctionType = it },
            selectedStatus = selectedStatus,
            onStatusSelected = { selectedStatus = it },
            lunchFilter = lunchFilter,
            onLunchFilterChanged = { lunchFilter = it },
            dinnerFilter = dinnerFilter,
            onDinnerFilterChanged = { dinnerFilter = it },
            balanceFilter = balanceFilter,
            onBalanceFilterChanged = { balanceFilter = it }
        )

        // Apply filters to events list
        val filteredEvents = events.filter { event ->
            (selectedBanquetLocation == null || event.banquetLocation == selectedBanquetLocation) &&
                    (!cocktailFilter || event.cocktail) &&
                    (!flowerFilter || event.flower) &&
                    (!djFilter || event.dj) &&
                    (selectedFoodType == null || event.foodType == selectedFoodType) &&
                    (selectedFunctionType == null || event.functionType == selectedFunctionType) &&
                    (selectedMenu == null || event.menu == selectedMenu) &&
                    (selectedStatus == null || event.status == selectedStatus) &&
                    (!lunchFilter || event.lunch) &&
                    (!dinnerFilter || event.dinner) &&
                    (balanceFilter == null || event.balance == balanceFilter)
        }

        // Display filtered events
        LazyColumn {
            items(filteredEvents) { event ->
                EventItem(event = event, navController = navController)
            }
        }
    }
}

@Composable
fun FilterSection(
    banquetLocations: List<String>,
    foodTypes: List<String>,
    functionTypes: List<String>,
    selectedBanquetLocation: String?,
    onBanquetLocationSelected: (String?) -> Unit,
    cocktailFilter: Boolean,
    onCocktailFilterChanged: (Boolean) -> Unit,
    flowerFilter: Boolean,
    onFlowerFilterChanged: (Boolean) -> Unit,
    djFilter: Boolean,
    onDjFilterChanged: (Boolean) -> Unit,
    selectedFoodType: String?,
    onFoodTypeSelected: (String?) -> Unit,
    selectedFunctionType: String?,
    onFunctionTypeSelected: (String?) -> Unit,
    selectedStatus: Long?,
    onStatusSelected: (Long?) -> Unit,
    lunchFilter: Boolean,
    onLunchFilterChanged: (Boolean) -> Unit,
    dinnerFilter: Boolean,
    onDinnerFilterChanged: (Boolean) -> Unit,
    balanceFilter: Long?,
    onBalanceFilterChanged: (Long?) -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        // Banquet Location Dropdown
        DropdownMenu(
            label = "Banquet Location",
            list = BanquetLocations.entries,
            selectedItem = BanquetLocations.SK_EASTEND,
            onItemSelected = { BanquetLocations.SK_EASTEND },
            displayName = { BanquetLocations.SK_EASTEND.displayName }
        )

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

        // DJ Filter
        CheckboxRow(
            label = "DJ",
            checked = djFilter,
            onCheckedChange = onDjFilterChanged
        )

        // Food Type Dropdown
//        DropdownMenu(
//            label = "Food Type",
//            items = foodTypes,
//            selectedItem = selectedFoodType,
//            onItemSelected = onFoodTypeSelected
//        )

        // Function Type Dropdown
//        DropdownMenu(
//            label = "Function Type",
//            items = functionTypes,
//            selectedItem = selectedFunctionType,
//            onItemSelected = onFunctionTypeSelected
//        )

        // Lunch Filter
        CheckboxRow(
            label = "Lunch",
            checked = lunchFilter,
            onCheckedChange = onLunchFilterChanged
        )

        // Dinner Filter
        CheckboxRow(
            label = "Dinner",
            checked = dinnerFilter,
            onCheckedChange = onDinnerFilterChanged
        )

        // Balance Input (TextField for entering balance)
//        BalanceInput(balance = balanceFilter, onBalanceChanged = onBalanceFilterChanged)
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
