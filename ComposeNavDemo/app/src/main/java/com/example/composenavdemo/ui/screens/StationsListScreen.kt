package com.example.composenavdemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.composenavdemo.data.database.StationWithAqi
import com.example.composenavdemo.ui.viewmodel.StationsListViewModel
import com.example.composenavdemo.utils.getAqiColor
import com.example.composenavdemo.utils.getAqiLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationsListScreen(
    viewModel: StationsListViewModel,
    onStationClick: (Long) -> Unit,
    onNavigateToAddStation: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToStationsList: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }

    val filteredStations = remember(searchQuery, selectedFilter, uiState.stations) {
        uiState.stations.filter { stationWithAqi ->
            val station = stationWithAqi.station
            val matchesSearch = station.name.contains(searchQuery, ignoreCase = true) ||
                    station.location.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                "Todos" -> true
                "Bueno" -> stationWithAqi.aqi != null && stationWithAqi.aqi <= 50
                "Moderado" -> stationWithAqi.aqi != null && stationWithAqi.aqi in 51..100
                "Dañino" -> stationWithAqi.aqi != null && stationWithAqi.aqi > 100
                else -> true
            }
            matchesSearch && matchesFilter
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Estaciones de Monitoreo")
                        Text(
                            text = "${filteredStations.size} de ${uiState.stations.size} encontradas",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Volver",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddStation,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Estación")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            SearchBar(query = searchQuery, onQueryChange = { searchQuery = it })
            FilterChips(selectedFilter = selectedFilter, onFilterSelected = { selectedFilter = it })

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = filteredStations, key = { it.station.id }) { stationWithAqi ->
                        StationListItem(
                            stationWithAqi = stationWithAqi,
                            onClick = { onStationClick(stationWithAqi.station.id) }
                        )
                    }
                    if (filteredStations.isEmpty()) {
                        item {
                            EmptyState(searchQuery = searchQuery)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("Buscar por nombre o ubicación...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        singleLine = true,
        shape = RoundedCornerShape(28.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
private fun FilterChips(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    val filters = listOf("Todos", "Bueno", "Moderado", "Dañino")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@Composable
private fun StationListItem(stationWithAqi: StationWithAqi, onClick: () -> Unit) {
    val station = stationWithAqi.station
    val aqi = stationWithAqi.aqi

    val aqiColor = if (aqi != null) getAqiColor(aqi) else MaterialTheme.colorScheme.onSurfaceVariant
    val aqiLabel = if (aqi != null) getAqiLabel(aqi) else "Sin datos"

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = station.name.take(2).uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    station.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    station.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = aqiColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = aqi?.toString() ?: "--",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = aqiColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = aqiLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = aqiColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun EmptyState(searchQuery: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("😕", style = MaterialTheme.typography.displaySmall)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No se encontraron estaciones",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (searchQuery.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Intenta con otra búsqueda",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
