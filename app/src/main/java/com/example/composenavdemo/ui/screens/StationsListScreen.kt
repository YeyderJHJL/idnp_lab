package com.example.composenavdemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composenavdemo.data.model.AirStation
import com.example.composenavdemo.data.model.AirStationDataSource

/**
 * Pantalla de lista de estaciones de monitoreo
 *
 * PRÁCTICA 5: Listas Dinámicas con LazyColumn
 * - Lista eficiente de 22 registros
 * - 4 elementos por ítem: ID, nombre, ubicación, AQI + avatar
 * - Scroll optimizado
 * - Búsqueda y filtrado
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationsListScreen(
    onNavigateBack: () -> Unit = {},
    onStationClick: (AirStation) -> Unit = {}
) {
    // Cargar datos
    val stations = remember { AirStationDataSource.generateStations() }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }

    // Filtrar estaciones según búsqueda y filtro
    val filteredStations = remember(searchQuery, selectedFilter, stations) {
        stations.filter { station ->
            val matchesSearch = station.name.contains(searchQuery, ignoreCase = true) ||
                    station.location.contains(searchQuery, ignoreCase = true) ||
                    station.id.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                "Todos" -> true
                "Bueno" -> station.aqi <= 50
                "Moderado" -> station.aqi in 51..100
                "Dañino" -> station.aqi > 100
                else -> true
            }

            matchesSearch && matchesFilter
        }
    }

    // Estado del scroll
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Estaciones de Monitoreo")
                        Text(
                            text = "${filteredStations.size} estaciones encontradas",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Búsqueda */ }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { /* TODO: Filtros */ }) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "Filtrar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AirSenseMint,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            // Barra de búsqueda
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )

            // Chips de filtro
            FilterChips(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            // Lista de estaciones con LazyColumn
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = filteredStations,
                    key = { station -> station.id }
                ) { station ->
                    StationListItem(
                        station = station,
                        onClick = { onStationClick(station) }
                    )
                }

                // Mensaje si no hay resultados
                if (filteredStations.isEmpty()) {
                    item {
                        EmptyState(
                            message = "No se encontraron estaciones",
                            searchQuery = searchQuery
                        )
                    }
                }
            }
        }
    }
}

/**
 * Barra de búsqueda
 */
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("Buscar por nombre, ubicación o ID...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        singleLine = true,
        shape = RoundedCornerShape(28.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AirSenseMint,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

/**
 * Chips de filtro
 */
@Composable
private fun FilterChips(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf("Todos", "Bueno", "Moderado", "Dañino")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AirSenseMint,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

/**
 * Item individual de la lista
 * Muestra: ID, Nombre, Ubicación, AQI + Avatar
 */
@Composable
private fun StationListItem(
    station: AirStation,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar con color personalizado
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(station.avatarColor)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = station.name.take(2).uppercase(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información de la estación
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // ID de la estación
                Text(
                    text = station.id,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = AirSenseMint
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Nombre de la estación
                Text(
                    text = station.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AirSenseDarkText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Ubicación
                Text(
                    text = station.location,
                    fontSize = 13.sp,
                    color = AirSenseLightText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Badge de AQI
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(station.status.color).copy(alpha = 0.2f)
                ) {
                    Text(
                        text = station.aqi.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(station.status.color),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = station.status.label,
                    fontSize = 10.sp,
                    color = Color(station.status.color),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Estado vacío cuando no hay resultados
 */
@Composable
private fun EmptyState(
    message: String,
    searchQuery: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔍",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = AirSenseDarkText
            )
            if (searchQuery.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Intenta con otra búsqueda",
                    fontSize = 14.sp,
                    color = AirSenseLightText
                )
            }
        }
    }
}