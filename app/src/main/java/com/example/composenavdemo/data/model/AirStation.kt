package com.example.composenavdemo.data.model

/**
 * Modelo de datos para Estación de Monitoreo de Calidad del Aire
 *
 * Representa una estación física que mide contaminantes en el aire
 */
data class AirStation(
    val id: Long,              // ID único de la estación
    val name: String,            // Nombre de la estación
    val location: String,        // Ubicación geográfica
    val aqi: Int,               // Índice de Calidad del Aire (0-500)
    val status: AirQualityStatus, // Estado de calidad
    val avatarColor: Long,       // Color del avatar (en formato 0xFFRRGGBB)
    val latitude: Double,        // Latitud geográfica
    val longitude: Double,       // Longitud geográfica
    val stationType: String      // Tipo de estación (ej: Urbana, Industrial)
)

/**
 * Estados de calidad del aire según EPA (Environmental Protection Agency)
 */
enum class AirQualityStatus(val label: String, val color: Long, val range: IntRange) {
    GOOD("Bueno", 0xFF4CAF50, 0..50),
    MODERATE("Moderado", 0xFFFFEB3B, 51..100),
    UNHEALTHY_SENSITIVE("Dañino para grupos sensibles", 0xFFFF9800, 101..150),
    UNHEALTHY("Dañino", 0xFFF44336, 151..200),
    VERY_UNHEALTHY("Muy dañino", 0xFF9C27B0, 201..300),
    HAZARDOUS("Peligroso", 0xFF7B1FA2, 301..500);

    companion object {
        fun fromAQI(aqi: Int): AirQualityStatus {
            return AirQualityStatus.entries.find { aqi in it.range } ?: HAZARDOUS
        }
    }
}

/**
 * Generador de datos de ejemplo
 */
object AirStationDataSource {

    private val locations = listOf(
        "Cercado, Arequipa",
        "Cayma, Arequipa",
        "Yanahuara, Arequipa",
        "Miraflores, Arequipa",
        "Paucarpata, Arequipa",
        "Cerro Colorado, Arequipa",
        "Hunter, Arequipa",
        "Socabaya, Arequipa",
        "Jacobo Hunter, Arequipa",
        "Sachaca, Arequipa",
        "Characato, Arequipa",
        "Sabandia, Arequipa",
        "Mariano Melgar, Arequipa",
        "José Luis Bustamante, Arequipa",
        "Alto Selva Alegre, Arequipa",
        "Tiabaya, Arequipa",
        "Uchumayo, Arequipa",
        "San Juan de Tarucani, Arequipa",
        "Mollebaya, Arequipa",
        "Yarabamba, Arequipa",
        "Quequeña, Arequipa",
        "Chiguata, Arequipa"
    )

    private val stationNames = listOf(
        "Estación Central", "Estación Norte", "Estación Sur", "Estación Este", "Estación Oeste",
        "Estación Industrial", "Estación Residencial", "Estación Comercial", "Estación Universitaria",
        "Estación Hospital", "Estación Plaza", "Estación Parque", "Estación Terminal",
        "Estación Mercado", "Estación Aeropuerto", "Estación Puerto", "Estación Río",
        "Estación Montaña", "Estación Valle", "Estación Ciudad", "Estación Pueblo", "Estación Rural"
    )

    // Datos añadidos para latitud, longitud y tipo de estación
    private val coordinates = listOf(
        -16.39889 to -71.5374, // Cercado
        -16.37889 to -71.55389, // Cayma
        -16.38889 to -71.54389, // Yanahuara
        -16.40139 to -71.5175,  // Miraflores
        -16.41667 to -71.5,     // Paucarpata
        -16.35861 to -71.58028, // Cerro Colorado
        -16.43333 to -71.55,    // Hunter
        -16.45 to -71.53333,    // Socabaya
        -16.43333 to -71.55,    // Jacobo Hunter
        -16.425 to -71.57139,    // Sachaca
        -16.48333 to -71.46667, // Characato
        -16.44306 to -71.50056, // Sabandia
        -16.41667 to -71.51667, // Mariano Melgar
        -16.41667 to -71.53333, // José Luis Bustamante
        -16.38333 to -71.51667, // Alto Selva Alegre
        -16.45 to -71.6,        // Tiabaya
        -16.41667 to -71.66667, // Uchumayo
        -15.9 to -71.25,        // San Juan de Tarucani
        -16.5 to -71.5,         // Mollebaya
        -16.55 to -71.46667,    // Yarabamba
        -16.51667 to -71.45,    // Quequeña
        -16.43333 to -71.38333  // Chiguata
    )

    private val stationTypes = listOf(
        "Urbana - Tráfico", "Suburbana", "Urbana - Residencial", "Urbana - Residencial",
        "Urbana", "Suburbana - Industrial", "Urbana - Residencial", "Rural",
        "Urbana", "Urbana - Residencial", "Rural", "Rural", "Urbana", "Urbana - Comercial",
        "Urbana", "Rural", "Industrial", "Rural", "Rural", "Rural", "Rural", "Rural"
    )


    /**
     * Devuelve la lista de estaciones (API pública)
     */
    fun getStations(): List<AirStation> {
        return generateStations()
    }

    /**
     * Genera una lista de 22 estaciones de monitoreo con datos variados
     */
    fun generateStations(): List<AirStation> {
        return locations.mapIndexed { index, location ->
            val aqi = when {
                index < 5 -> (20..50).random()      // Bueno
                index < 10 -> (51..100).random()    // Moderado
                index < 15 -> (101..150).random()   // Dañino sensibles
                index < 18 -> (151..200).random()   // Dañino
                index < 21 -> (201..300).random()   // Muy dañino
                else -> (301..400).random()         // Peligroso
            }

            val status = AirQualityStatus.fromAQI(aqi)

            AirStation(
                id = index.toLong() + 1, // Corregido: ID ahora es Long
                name = stationNames[index],
                location = location,
                aqi = aqi,
                status = status,
                avatarColor = generateAvatarColor(index),
                latitude = coordinates[index].first,  // Añadido
                longitude = coordinates[index].second, // Añadido
                stationType = stationTypes[index]      // Añadido
            )
        }
    }

    /**
     * Busca y devuelve una estación por su ID.
     * @param id El ID de la estación a buscar.
     * @return La [AirStation] si se encuentra, o null si no.
     */
    fun getStationById(id: Long): AirStation? { // Corregido: ID ahora es Long
        return generateStations().find { it.id == id }
    }

    /**
     * Genera colores variados para los avatares
     */
    private fun generateAvatarColor(index: Int): Long {
        val colors = listOf(
            0xFF5FCCBB, 0xFF64B5F6, 0xFF81C784, 0xFFFFB74D, 0xFFF06292, 0xFF9575CD,
            0xFF4DB6AC, 0xFFFFD54F, 0xFFE57373, 0xFF7986CB, 0xFF4DD0E1, 0xFFAED581
        )
        return colors[index % colors.size]
    }
}