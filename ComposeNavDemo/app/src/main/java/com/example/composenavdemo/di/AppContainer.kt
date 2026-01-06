package com.example.composenavdemo.di

import android.content.Context
import com.example.composenavdemo.data.database.AirQualityDatabase
import com.example.composenavdemo.data.preferences.PreferencesManager
import com.example.composenavdemo.data.repository.AirQualityRepository

// El contrato que define lo que ofrece el contenedor
interface AppContainer {
    val airQualityRepository: AirQualityRepository
    val preferencesManager: PreferencesManager
}

// La implementación real que construye las dependencias
class DefaultAppContainer(private val context: Context) : AppContainer {

    // El repositorio que ya existía
    override val airQualityRepository: AirQualityRepository by lazy {
        AirQualityRepository(AirQualityDatabase.getDatabase(context).monitoringStationDao(), AirQualityDatabase.getDatabase(context).airQualityMeasurementDao())
    }

    // El gestor de preferencias que faltaba
    override val preferencesManager: PreferencesManager by lazy {
        PreferencesManager(context)
    }
}