package com.example.composenavdemo.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Clase de datos que combina una estación con su última medición de AQI.
 * Room utilizará la consulta personalizada para poblar esta clase.
 */
data class StationWithAqi(
    @Embedded
    val station: MonitoringStationEntity,
    val aqi: Int? // Puede ser nulo si no hay mediciones para la estación
)

/**
 * DAO para operaciones de Estaciones de Monitoreo
 *
 * PRÁCTICA 7: Room Database
 * Define todas las operaciones CRUD para estaciones
 */
@Dao
interface MonitoringStationDao {

    /**
     * Obtiene una lista de todas las estaciones junto con su última medición de AQI.
     * Esta consulta es la base para la nueva interfaz de lista de estaciones.
     */
    @Query("""
        SELECT s.*,
               (SELECT aqi
                FROM air_quality_measurements
                WHERE stationId = s.id
                ORDER BY timestamp DESC
                LIMIT 1) as aqi
        FROM monitoring_stations s
        ORDER BY s.name ASC
    """)
    fun getStationsWithLatestAqi(): Flow<List<StationWithAqi>>

    /**
     * Insertar una nueva estación
     * @return ID de la estación insertada
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStation(station: MonitoringStationEntity): Long

    /**
     * Insertar múltiples estaciones
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<MonitoringStationEntity>)

    /**
     * Actualizar una estación existente
     */
    @Update
    suspend fun updateStation(station: MonitoringStationEntity)

    /**
     * Eliminar una estación
     */
    @Delete
    suspend fun deleteStation(station: MonitoringStationEntity)

    /**
     * Obtener todas las estaciones (Flow para observar cambios)
     */
    @Query("SELECT * FROM monitoring_stations ORDER BY name ASC")
    fun getAllStations(): Flow<List<MonitoringStationEntity>>

    /**
     * Obtener estaciones activas
     */
    @Query("SELECT * FROM monitoring_stations WHERE isActive = 1 ORDER BY name ASC")
    fun getActiveStations(): Flow<List<MonitoringStationEntity>>

    /**
     * Obtener una estación por ID (como Flow para observar cambios)
     */
    @Query("SELECT * FROM monitoring_stations WHERE id = :stationId")
    fun getStationById(stationId: Long): Flow<MonitoringStationEntity?>

    /**
     * Buscar estaciones por nombre o ubicación
     */
    @Query("""
        SELECT * FROM monitoring_stations 
        WHERE name LIKE '%' || :query || '%' 
           OR location LIKE '%' || :query || '%'
        ORDER BY name ASC
    """)
    fun searchStations(query: String): Flow<List<MonitoringStationEntity>>

    /**
     * Obtener estaciones por tipo
     */
    @Query("SELECT * FROM monitoring_stations WHERE stationType = :type")
    fun getStationsByType(type: String): Flow<List<MonitoringStationEntity>>

    /**
     * Contar total de estaciones
     */
    @Query("SELECT COUNT(*) FROM monitoring_stations")
    suspend fun getStationsCount(): Int

    /**
     * Eliminar todas las estaciones
     */
    @Query("DELETE FROM monitoring_stations")
    suspend fun deleteAllStations()
}

/**
 * DAO para operaciones de Mediciones de Calidad del Aire
 */
@Dao
interface AirQualityMeasurementDao {

    /**
     * Insertar una nueva medición
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurement: AirQualityMeasurementEntity): Long

    /**
     * Insertar múltiples mediciones
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurements(measurements: List<AirQualityMeasurementEntity>)

    /**
     * Obtener todas las mediciones de una estación
     */
    @Query("""
        SELECT * FROM air_quality_measurements 
        WHERE stationId = :stationId 
        ORDER BY timestamp DESC
    """)
    fun getMeasurementsByStation(stationId: Long): Flow<List<AirQualityMeasurementEntity>>

    /**
     * Obtener últimas N mediciones de una estación
     */
    @Query("""
        SELECT * FROM air_quality_measurements 
        WHERE stationId = :stationId 
        ORDER BY timestamp DESC 
        LIMIT :limit
    """)
    fun getRecentMeasurements(stationId: Long, limit: Int): Flow<List<AirQualityMeasurementEntity>>

    /**
     * Obtener mediciones en un rango de tiempo
     */
    @Query("""
        SELECT * FROM air_quality_measurements 
        WHERE stationId = :stationId 
          AND timestamp BETWEEN :startTime AND :endTime
        ORDER BY timestamp ASC
    """)
    fun getMeasurementsInRange(
        stationId: Long,
        startTime: Long,
        endTime: Long
    ): Flow<List<AirQualityMeasurementEntity>>

    /**
     * Obtener la última medición de una estación (como Flow para observar cambios)
     */
    @Query("""
        SELECT * FROM air_quality_measurements 
        WHERE stationId = :stationId 
        ORDER BY timestamp DESC 
        LIMIT 1
    """)
    fun getLatestMeasurement(stationId: Long): Flow<AirQualityMeasurementEntity?>

    /**
     * Obtener estadísticas de una estación
     */
    @Query("""
        SELECT 
            s.id as stationId,
            s.name as stationName,
            COUNT(m.id) as totalMeasurements,
            AVG(m.aqi) as avgAqi,
            MAX(m.aqi) as maxAqi,
            MIN(m.aqi) as minAqi,
            MAX(m.timestamp) as lastMeasurementTime
        FROM monitoring_stations s
        LEFT JOIN air_quality_measurements m ON s.id = m.stationId
        WHERE s.id = :stationId
        GROUP BY s.id
    """)
    suspend fun getStationStatistics(stationId: Long): StationStatistics?

    /**
     * Eliminar mediciones antiguas (mantener solo últimos N días)
     */
    @Query("""
        DELETE FROM air_quality_measurements 
        WHERE timestamp < :cutoffTime
    """)
    suspend fun deleteOldMeasurements(cutoffTime: Long): Int

    /**
     * Eliminar todas las mediciones de una estación
     */
    @Query("DELETE FROM air_quality_measurements WHERE stationId = :stationId")
    suspend fun deleteMeasurementsByStation(stationId: Long)

    /**
     * Contar mediciones de una estación
     */
    @Query("SELECT COUNT(*) FROM air_quality_measurements WHERE stationId = :stationId")
    suspend fun getMeasurementsCount(stationId: Long): Int
}