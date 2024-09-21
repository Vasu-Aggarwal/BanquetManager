package com.android.banquetmanager.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.FilterAltOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.model.Payment
import com.android.banquetmanager.data.viewmodel.BookingViewmodel
import com.android.banquetmanager.utils.AppConstants
import com.android.banquetmanager.utils.BanquetLocations
import com.android.banquetmanager.utils.FoodType
import com.android.banquetmanager.utils.FunctionType
import com.android.banquetmanager.utils.Menu

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

    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var isFilterVisible by remember { mutableStateOf(false) } // Control visibility of the filter section
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(Unit) {
        val fetchedEvents = viewModel.getEventsByMonthAndYear(9, 2024)
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
                EventDetailsCard(event = event)
            }
        }
    }
}


@Composable
fun FilterSection(
    isFilterVisible: Boolean,
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
    onBalanceFilterChanged: (Long?) -> Unit
) {

    Column(Modifier.padding(16.dp)) {
        // Filter section visibility controlled by isFilterVisible
        if (isFilterVisible) {

            // Use LazyColumn for a scrollable list of filters
            LazyColumn(
                modifier = Modifier.height(500.dp)
            ) {
                item {
                    // Banquet Location Checkboxes (dynamically generated)
                    Text(text = "Banquet Locations:", fontSize = AppConstants.SUBHEADING_TEXT.sp)
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
                    Text(text = "Food Type:", fontSize = AppConstants.SUBHEADING_TEXT.sp)
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
                    Text(text = "Function Type:", fontSize = AppConstants.SUBHEADING_TEXT.sp)
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 100.dp), // Adjust cell size as needed
                        contentPadding = PaddingValues(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
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
                    Text(text = "Menu Type:", fontSize = AppConstants.SUBHEADING_TEXT.sp)
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 100.dp), // Adjust cell size as needed
                        contentPadding = PaddingValues(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
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
                    Text(text = "Other:", fontSize = AppConstants.SUBHEADING_TEXT.sp)
                    // List of filter items
                    val filters = listOf(
                        FilterItem("Cocktail", cocktailFilter, onCocktailFilterChanged),
                        FilterItem("Flower", flowerFilter, onFlowerFilterChanged),
                        FilterItem("DJ", djFilter, onDjFilterChanged),
                        FilterItem("Lunch", lunchFilter, onLunchFilterChanged),
                        FilterItem("Dinner", dinnerFilter, onDinnerFilterChanged)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 120.dp), // Adjust cell size as needed
                        contentPadding = PaddingValues(5.dp),
                        modifier = Modifier.height(150.dp),
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

data class FilterItem(
    val label: String,
    val isChecked: Boolean,
    val onCheckedChange: (Boolean) -> Unit
)
