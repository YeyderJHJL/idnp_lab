package com.example.composenavdemo.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Entidad para Estación de Monitoreo
 *
 * PRÁCTICA 7: Room Database
 * Representa una estación física que mide calidad del aire
 */
@Entity(
    tableName = "monitoring_stations"
)
data class MonitoringStationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val installationDate: Long, // timestamp en milisegundos
    val isActive: Boolean = true,
    val stationType: String, // "URBAN", "INDUSTRIAL", "RESIDENTIAL"
    val description: String = ""
)

/**
 * Entidad para Medición de Calidad del Aire
 *
 * Cada registro representa una medición en un momento específico
 * Relación: Muchas mediciones -> Una estación
 */
@Entity(
    tableName = "air_quality_measurements",
    foreignKeys = [
        ForeignKey(
            entity = MonitoringStationEntity::class,
            parentColumns = ["id"],
            childColumns = ["stationId"],
            onDelete = ForeignKey.CASCADE // Si se borra estación, se borran mediciones
        )
    ],
    indices = [Index(value = ["stationId"])] // Índice para búsquedas rápidas
)
data class AirQualityMeasurementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val stationId: Long, // FK a monitoring_stations
    val timestamp: Long, // Momento de la medición

    // Contaminantes (en μg/m³)
    val pm25: Double,    // Partículas finas
    val pm10: Double,    // Partículas gruesas
    val no2: Double,     // Dióxido de nitrógeno
    val so2: Double,     // Dióxido de azufre
    val co: Double,      // Monóxido de carbono
    val o3: Double,      // Ozono

    // Condiciones ambientales
    val temperature: Double,
    val humidity: Double,
    val pressure: Double,

    // Índice calculado
    val aqi: Int         // Air Quality Index (0-500)
)

/**
 * Data class para JOIN entre estación y sus mediciones
 * No es una entidad de Room, sino un resultado de consulta
 */
data class StationWithMeasurements(
    val station: MonitoringStationEntity,
    val measurements: List<AirQualityMeasurementEntity>
)

/**
 * Data class para estadísticas de una estación
 */
data class StationStatistics(
    val stationId: Long,
    val stationName: String,
    val totalMeasurements: Int,
    val avgAqi: Double,
    val maxAqi: Int,
    val minAqi: Int,
    val lastMeasurementTime: Long
)