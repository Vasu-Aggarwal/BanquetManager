package com.android.banquetmanager.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.outlined.FilterAltOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.viewmodel.BookingViewmodel
import com.android.banquetmanager.utils.AppConstants
import com.android.banquetmanager.utils.BanquetLocations
import com.android.banquetmanager.utils.FoodType
import com.android.banquetmanager.utils.FunctionType
import com.android.banquetmanager.utils.Menu
import com.android.banquetmanager.utils.Months
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
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
    var showPaymentDetails by remember { mutableStateOf(false) }
    var fruitFilter by remember { mutableStateOf(false) }
    var threeSixtyFilter by remember { mutableStateOf(false) }
    var extraEventFilter by remember { mutableStateOf(false) }

    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var isFilterVisible by remember { mutableStateOf(false) } // Control visibility of the filter section
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var selectedMonth by remember { mutableStateOf(9) } // Default to September (9)
    var selectedYear by remember { mutableStateOf(2024) } // Default to the year 2024

    // Available options for month and year
    val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    val years = (2020..2030).map { it.toString() } // Change range as per your needs

    LaunchedEffect(selectedMonth, selectedYear) {
        val fetchedEvents = viewModel.getEventsByMonthAndYear(selectedMonth, selectedYear)
        // Update the state with the fetched events
        events = fetchedEvents
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Apply Filters",
                        style = MaterialTheme.typography.titleLarge, // Adjust text style if needed
                    )
                },
//                navigationIcon = {
//                    IconButton(onClick = {
//                        navController.popBackStack()
//                    }) {
//                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                    }
//                },
                actions = {
//                      Button(onClick = { isFilterVisible = !isFilterVisible }) {
//                          Icon(
//                              imageVector = if (isFilterVisible) Icons.Default.FilterAlt else Icons.Outlined.FilterAltOff,
//                              contentDescription = if (isFilterVisible) "Hide filters" else "Show filters"
//                          )
//                      }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            // Floating Action Button at the bottom right
            FloatingActionButton(
                onClick = {
                    isFilterVisible = !isFilterVisible
                },
                modifier = Modifier
                    .padding(16.dp), // Add padding to keep FAB away from the edge of the screen
            ) {
                Icon(
                    imageVector = if (isFilterVisible) Icons.Default.FilterAlt else Icons.Outlined.FilterAltOff,
                    contentDescription = if (isFilterVisible) "Hide filters" else "Show filters"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                FilterSection(
                    isFilterVisible,
                    showPaymentDetails,
                    onShowPaymentDetailsChanged = { showPaymentDetails = it },
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
                    },
                    fruitFilter = fruitFilter,
                    onFruitFilterSelected = { fruitFilter = it },
                    threeSixtyFilter = threeSixtyFilter,
                    onThreeSixtyFilterSelected = { threeSixtyFilter = it },
                    extraEventFilter = extraEventFilter,
                    onExtraEventFilterSelected = { extraEventFilter = it },
                    selectedMonth = months[selectedMonth-1],
                    onMonthSelected = {monthName -> selectedMonth = months.indexOf(monthName) + 1},
                    selectedYear = selectedYear.toString(),
                    onYearSelected = { selectedYear = it.toInt() }
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
                        (balanceFilter == null || event.balance == balanceFilter) &&
                        (!fruitFilter || event.fruit) &&
                        (!threeSixtyFilter || event.threeSixty) &&
                        (!extraEventFilter || event.extraEvents)
            }) { event ->
                EventDetailsCard(
                    event = event,
                    bookingViewmodel = viewModel,
                    showPaymentDetails = showPaymentDetails
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // Height to prevent overlap with FAB
            }
        }
    }
}


@Composable
fun FilterSection(
    isFilterVisible: Boolean,
    showPaymentDetails: Boolean,
    onShowPaymentDetailsChanged: (Boolean) -> Unit,
    selectedBanquetLocations: List<String>,
    onBanquetLocationSelected: (List<String>) -> Unit,
    selectedFoodType: List<String>,
    onFoodTypeSelected: (List<String>) -> Unit,
    selectedFunctionType: List<String>,
    onFunctionTypeSelected: (List<String>) -> Unit,
    selectedMenuType: List<String>,
    onMenuTypeSelected: (List<String>) -> Unit,
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
    onBalanceFilterChanged: (Long?) -> Unit,
    selectedMonth: String,
    onMonthSelected: (String) -> Unit,
    selectedYear: String,
    onYearSelected: (String) -> Unit,
    fruitFilter : Boolean,
    onFruitFilterSelected : (Boolean) -> Unit,
    threeSixtyFilter : Boolean,
    onThreeSixtyFilterSelected : (Boolean) -> Unit,
    extraEventFilter : Boolean,
    onExtraEventFilterSelected : (Boolean) -> Unit,
) {

    Column(Modifier.padding(16.dp)) {
        // Filter section visibility controlled by isFilterVisible
        if (isFilterVisible) {

            // Use LazyColumn for a scrollable list of filters
            LazyColumn(
                modifier = Modifier.height(500.dp)
            ) {
                item {

                    // Month Dropdown
                    DropdownMenuField(
                        label = "Select Month",
                        options = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"),
                        selectedOption = selectedMonth,
                        onOptionSelected = onMonthSelected
                    )

                    // Year Dropdown
                    DropdownMenuField(
                        label = "Select Year",
                        options = (2020..2030).map { it.toString() }, // Generates a list of years from 2020 to 2030
                        selectedOption = selectedYear,
                        onOptionSelected = onYearSelected
                    )

                    // Banquet Location Checkboxes (dynamically generated)
                    Text(text = "Banquet Locations:", fontSize = AppConstants.SUBHEADING_TEXT.sp, fontWeight = FontWeight.W500)
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 100.dp), // Adjust cell size as needed
                        contentPadding = PaddingValues(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(BanquetLocations.entries.toList()) { type ->
                            val isChecked = selectedBanquetLocations.contains(type.name)
                            CheckboxRow(
                                label = type.displayName,
                                checked = isChecked,
                                onCheckedChange = { isSelected ->
                                    val updatedSelections = if (isSelected) {
                                        selectedBanquetLocations + type.name
                                    } else {
                                        selectedBanquetLocations - type.name
                                    }
                                    onBanquetLocationSelected(updatedSelections)
                                }
                            )
                        }
                    }
                }

                item {
                    // Food Type Checkboxes (dynamically generated)
                    Text(text = "Food Type:", fontSize = AppConstants.SUBHEADING_TEXT.sp, fontWeight = FontWeight.W500)
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 100.dp), // Adjust cell size as needed
                        contentPadding = PaddingValues(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(FoodType.entries.toList()) { type ->
                            val isChecked = selectedFoodType.contains(type.name)
                            CheckboxRow(
                                label = type.displayName,
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
                }

                item {
                    // Function Type Checkboxes (dynamically generated)
                    Text(text = "Function Type:", fontSize = AppConstants.SUBHEADING_TEXT.sp, fontWeight = FontWeight.W500)
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 120.dp), // Adjust cell size as needed
                        contentPadding = PaddingValues(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(FunctionType.entries.toList()) { type ->
                            val isChecked = selectedFunctionType.contains(type.name)
                            CheckboxRow(
                                label = type.displayName,
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
                }

                item {
                    // Menu Type Checkboxes displayed in a LazyVerticalGrid
                    Text(text = "Menu Type:", fontSize = AppConstants.SUBHEADING_TEXT.sp, fontWeight = FontWeight.W500)
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 100.dp), // Adjust cell size as needed
                        contentPadding = PaddingValues(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(Menu.entries.toList()) { type ->
                            val isChecked = selectedMenuType.contains(type.name)
                            CheckboxRow(
                                label = type.displayName,
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
                }

                item {
                    Text(text = "Other:", fontSize = AppConstants.SUBHEADING_TEXT.sp, fontWeight = FontWeight.W500)
                    // List of filter items
                    val filters = listOf(
                        FilterItem("Cocktail", cocktailFilter, onCocktailFilterChanged),
                        FilterItem("Flower", flowerFilter, onFlowerFilterChanged),
                        FilterItem("DJ", djFilter, onDjFilterChanged),
                        FilterItem("Lunch", lunchFilter, onLunchFilterChanged),
                        FilterItem("Dinner", dinnerFilter, onDinnerFilterChanged),
                        FilterItem("Payment Details", showPaymentDetails, onShowPaymentDetailsChanged),
                        FilterItem("Fruit", fruitFilter, onFruitFilterSelected),
                        FilterItem("360", threeSixtyFilter, onThreeSixtyFilterSelected),
                        FilterItem("Extra Events", extraEventFilter, onExtraEventFilterSelected)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 120.dp), // Adjust cell size as needed
                        contentPadding = PaddingValues(5.dp),
                        modifier = Modifier.height(250.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filters) { filter ->
                            CheckboxRow(
                                label = filter.label,
                                checked = filter.isChecked,
                                onCheckedChange = filter.onCheckedChange
                            )
                        }
                    }
                }
            }
            HorizontalDivider()
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
            .clickable { onCheckedChange(!checked) } // Click the whole row to toggle the checkbox
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(text = label, fontSize = AppConstants.NORMAL_TEXT.sp)
    }
}

@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label, fontSize = AppConstants.SUBHEADING_TEXT.sp, fontWeight = FontWeight.W500)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(8.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = selectedOption,
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (!expanded)
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
                else
                    Icon(imageVector = Icons.Default.ArrowDropUp, contentDescription = "")
            }
        }

        androidx.compose.material3.DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                androidx.compose.material.DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                }) {
                    Text(text = option)
                }
            }
        }
    }
}

data class FilterItem(
    val label: String,
    val isChecked: Boolean,
    val onCheckedChange: (Boolean) -> Unit
)
