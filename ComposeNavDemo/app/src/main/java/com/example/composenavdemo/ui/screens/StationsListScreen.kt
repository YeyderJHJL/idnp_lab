package com.example.composenavdemo.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.composenavdemo.ui.viewmodel.StationsListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationsListScreen(
    viewModel: StationsListViewModel,
    onStationClick: (Long) -> Unit,
    onNavigateToAddStation: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToStationsList: () -> Unit // Added for consistency with navGraph, though unused
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estaciones de Monitoreo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
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
            FloatingActionButton(onClick = onNavigateToAddStation) {
                Icon(Icons.Default.Add, contentDescription = "Add Station")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.stations.isEmpty()) {
                Text("No stations found. Tap the + button to add one.", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.stations) { station ->
                        ListItem(
                            headlineContent = { Text(station.name) },
                            supportingContent = { Text(station.location) },
                            modifier = Modifier.clickable { onStationClick(station.id) }
                        )
                    }
                }
            }
        }
    }
}