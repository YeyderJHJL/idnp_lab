package com.example.composenavdemo.di

import android.content.Context
import com.example.composenavdemo.data.database.AirQualityDatabase
import com.example.composenavdemo.data.repository.AirQualityRepository

/**
 * Contenedor de dependencias para la aplicación, sigue el patrón de Inyección de Dependencias manual.
 */
interface AppContainer {
    val airQualityRepository: AirQualityRepository
}

/**
 * Implementación por defecto del contenedor de dependencias.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    // La instancia del repositorio se crea de forma perezosa (lazy) la primera vez que se necesita.
    override val airQualityRepository: AirQualityRepository by lazy {
        val database = AirQualityDatabase.getDatabase(context)
        AirQualityRepository(
            stationDao = database.monitoringStationDao(),
            measurementDao = database.airQualityMeasurementDao()
        )
    }
}
