package com.example.composenavdemo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.composenavdemo.data.model.AirStationDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

// No se importan los DAO de .data.dao
// Se usan los definidos en DAOs.kt en este mismo paquete

@Database(
    entities = [MonitoringStationEntity::class, AirQualityMeasurementEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AirQualityDatabase : RoomDatabase() {
    abstract fun monitoringStationDao(): MonitoringStationDao
    abstract fun airQualityMeasurementDao(): AirQualityMeasurementDao

    companion object {
        @Volatile
        private var Instance: AirQualityDatabase? = null

        fun getDatabase(context: Context): AirQualityDatabase {
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AirQualityDatabase::class.java,
                    "air_quality_database"
                )
                    .addCallback(AirQualityDatabaseCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()
                Instance = instance
                instance
            }
        }
    }

    private class AirQualityDatabaseCallback(
        private val context: Context,
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Instance?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.monitoringStationDao(), database.airQualityMeasurementDao())
                }
            }
        }

        suspend fun populateDatabase(
            monitoringStationDao: MonitoringStationDao,
            airQualityMeasurementDao: AirQualityMeasurementDao
        ) {
            // Obtiene las estaciones de ejemplo desde la fuente de la verdad
            val stations = AirStationDataSource.getStations()

            // 1. Mapea AirStation a MonitoringStationEntity, usando todos los campos
            val stationEntities = stations.map { airStation ->
                MonitoringStationEntity(
                    id = airStation.id,
                    name = airStation.name,
                    location = airStation.location,
                    latitude = airStation.latitude,
                    longitude = airStation.longitude,
                    installationDate = Date().time, // Usamos una fecha de instalación de ejemplo
                    stationType = airStation.stationType,
                )
            }

            // 2. Llama a la función correcta para insertar la lista completa
            monitoringStationDao.insertStations(stationEntities)

            // 3. Por cada estación, inserta una medición de ejemplo completa
            stationEntities.forEach { stationEntity ->
                val originalAirStation = stations.find { it.id == stationEntity.id }!!

                airQualityMeasurementDao.insertMeasurement(
                    AirQualityMeasurementEntity(
                        stationId = stationEntity.id,
                        aqi = originalAirStation.aqi,
                        so2 = Math.random() * 10,
                        no2 = Math.random() * 20,
                        o3 = Math.random() * 50,
                        co = Math.random() * 2,
                        pm25 = Math.random() * 25,
                        pm10 = Math.random() * 50,
                        temperature = 15 + Math.random() * 10,
                        humidity = 30 + Math.random() * 40,
                        pressure = 1010 + Math.random() * 20,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }
}
