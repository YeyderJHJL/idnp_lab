package com.example.composenavdemo.data.repository

import com.example.composenavdemo.data.database.AirQualityMeasurementDao
import com.example.composenavdemo.data.database.MonitoringStationDao
import com.example.composenavdemo.data.database.AirQualityMeasurementEntity
import com.example.composenavdemo.data.database.MonitoringStationEntity
import com.example.composenavdemo.data.database.StationWithAqi
import kotlinx.coroutines.flow.Flow

/**
 * Repository para operaciones de calidad del aire.
 * Capa de abstracción entre la UI y la fuente de datos (DAO).
 * Expone solo los datos y operaciones que la aplicación necesita.
 */
class AirQualityRepository(
    private val stationDao: MonitoringStationDao,
    private val measurementDao: AirQualityMeasurementDao
) {

    /**
     * Obtiene un Flow con la lista de todas las estaciones y su última medición de AQI.
     * Esta es la función que usará el ViewModel para la pantalla de la lista.
     */
    fun getStationsWithAqi(): Flow<List<StationWithAqi>> {
        return stationDao.getStationsWithLatestAqi()
    }

    /**
     * Obtiene un Flow con la lista de todas las estaciones de monitoreo.
     */
    fun getStations(): Flow<List<MonitoringStationEntity>> {
        return stationDao.getAllStations()
    }

    /**
     * Obtiene una estación específica por su ID como un Flow.
     */
    fun getStationById(id: Long): Flow<MonitoringStationEntity?> {
        return stationDao.getStationById(id)
    }

    /**
     * Obtiene un Flow con la última medición de calidad del aire para una estación específica.
     */
    fun getLatestMeasurementForStation(stationId: Long): Flow<AirQualityMeasurementEntity?> {
        return measurementDao.getLatestMeasurement(stationId)
    }

    /**
     * Inserta una nueva estación de monitoreo en la base de datos.
     */
    suspend fun insertStation(station: MonitoringStationEntity) {
        stationDao.insertStation(station)
    }
}
