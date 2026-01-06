package com.example.composenavdemo.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.composenavdemo.data.database.MonitoringStationEntity
import com.example.composenavdemo.data.repository.AirQualityRepository
import kotlinx.coroutines.launch

/**
 * UI state for the Add Station screen
 */
data class AddStationUiState(
    val name: String = "",
    val location: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val isActive: Boolean = true,
    val stationType: String = "URBAN",
    val description: String = ""
)

/**
 * ViewModel for the Add Station screen.
 */
class AddStationViewModel(private val repository: AirQualityRepository) : ViewModel() {

    var uiState by mutableStateOf(AddStationUiState())
        private set

    fun updateName(name: String) {
        uiState = uiState.copy(name = name)
    }

    fun updateLocation(location: String) {
        uiState = uiState.copy(location = location)
    }

    fun updateLatitude(latitude: String) {
        uiState = uiState.copy(latitude = latitude)
    }

    fun updateLongitude(longitude: String) {
        uiState = uiState.copy(longitude = longitude)
    }

    fun updateIsActive(isActive: Boolean) {
        uiState = uiState.copy(isActive = isActive)
    }

    fun updateStationType(stationType: String) {
        uiState = uiState.copy(stationType = stationType)
    }

    fun updateDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    fun saveStation(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val newStation = MonitoringStationEntity(
                name = uiState.name,
                location = uiState.location,
                latitude = uiState.latitude.toDoubleOrNull() ?: 0.0,
                longitude = uiState.longitude.toDoubleOrNull() ?: 0.0,
                installationDate = System.currentTimeMillis(), // Set current timestamp
                isActive = uiState.isActive,
                stationType = uiState.stationType,
                description = uiState.description
            )
            repository.insertStation(newStation)
            onSuccess()
        }
    }

    companion object {
        fun provideFactory(repository: AirQualityRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AddStationViewModel(repository)
            }
        }
    }
}