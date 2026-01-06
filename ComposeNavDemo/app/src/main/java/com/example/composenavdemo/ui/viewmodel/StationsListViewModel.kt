package com.example.composenavdemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.composenavdemo.data.database.StationWithAqi
import com.example.composenavdemo.data.repository.AirQualityRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Representa el estado de la UI para la pantalla de la lista de estaciones.
 * Ahora contiene la lista de 'StationWithAqi' para tener la información completa.
 */
data class StationsListUiState(
    val stations: List<StationWithAqi> = emptyList(),
    val isLoading: Boolean = true // Por defecto, está cargando hasta que el primer flujo llegue
)

/**
 * ViewModel para la pantalla que muestra la lista de estaciones de monitoreo.
 */
class StationsListViewModel(repository: AirQualityRepository) : ViewModel() {

    // Convierte el Flow de la base de datos en un StateFlow que la UI puede consumir.
    // Llama al nuevo método del repositorio para obtener los datos completos.
    val uiState: StateFlow<StationsListUiState> = repository.getStationsWithAqi()
        .map { stationsWithAqi ->
            StationsListUiState(stations = stationsWithAqi, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StationsListUiState(isLoading = true) // El estado inicial es 'cargando'
        )

    companion object {
        /**
         * Factory para crear una instancia de StationsListViewModel con dependencias.
         */
        fun provideFactory(
            repository: AirQualityRepository
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(StationsListViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return StationsListViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
