package com.example.composenavdemo.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.composenavdemo.data.database.AirQualityMeasurementEntity
import com.example.composenavdemo.data.database.MonitoringStationEntity
import com.example.composenavdemo.data.repository.AirQualityRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * Estado de la UI para la pantalla de detalles de la estación.
 */
data class StationDetailsUiState(
    val station: MonitoringStationEntity? = null,
    val lastMeasurement: AirQualityMeasurementEntity? = null,
    val isLoading: Boolean = true
)

class StationDetailsViewModel(
    repository: AirQualityRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val stationId: Long = checkNotNull(savedStateHandle["stationId"])

    // Flujos de datos individuales obtenidos del repositorio.
    private val stationFlow = repository.getStationById(stationId)
    private val measurementFlow = repository.getLatestMeasurementForStation(stationId)

    /**
     * Combina los flujos de la estación y la medición en un único StateFlow de UI.
     * La pantalla observará este estado para actualizarse.
     */
    val uiState: StateFlow<StationDetailsUiState> = 
        combine(stationFlow, measurementFlow) { station, measurement ->
            StationDetailsUiState(
                station = station,
                lastMeasurement = measurement,
                isLoading = false // La carga termina cuando ambos flujos emiten su primer valor.
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StationDetailsUiState(isLoading = true) // Estado inicial mientras se cargan los datos.
        )

    companion object {
        // Fábrica para crear el ViewModel con sus dependencias.
        fun provideFactory(
            repository: AirQualityRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                return StationDetailsViewModel(
                    repository = repository,
                    savedStateHandle = extras.createSavedStateHandle()
                ) as T
            }
        }
    }
}
