package com.example.composenavdemo.data.model

/**
 * Modelo de datos para Estación de Monitoreo de Calidad del Aire
 *
 * Representa una estación física que mide contaminantes en el aire
 */
data class AirStation(
    val id: String,              // ID único de la estación
    val name: String,            // Nombre de la estación
    val location: String,        // Ubicación geográfica
    val aqi: Int,               // Índice de Calidad del Aire (0-500)
    val status: AirQualityStatus, // Estado de calidad
    val avatarColor: Long       // Color del avatar (en formato 0xFFRRGGBB)
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
            return values().find { aqi in it.range } ?: HAZARDOUS
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
        "Estación Central",
        "Estación Norte",
        "Estación Sur",
        "Estación Este",
        "Estación Oeste",
        "Estación Industrial",
        "Estación Residencial",
        "Estación Comercial",
        "Estación Universitaria",
        "Estación Hospital",
        "Estación Plaza",
        "Estación Parque",
        "Estación Terminal",
        "Estación Mercado",
        "Estación Aeropuerto",
        "Estación Puerto",
        "Estación Río",
        "Estación Montaña",
        "Estación Valle",
        "Estación Ciudad",
        "Estación Pueblo",
        "Estación Rural"
    )

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
                id = "EST-${String.format("%03d", index + 1)}",
                name = stationNames[index],
                location = location,
                aqi = aqi,
                status = status,
                avatarColor = generateAvatarColor(index)
            )
        }
    }

    /**
     * Busca y devuelve una estación por su ID.
     * @param id El ID de la estación a buscar.
     * @return La [AirStation] si se encuentra, o null si no.
     */
    fun getStationById(id: String): AirStation? {
        return generateStations().find { it.id == id }
    }

    /**
     * Genera colores variados para los avatares
     */
    private fun generateAvatarColor(index: Int): Long {
        val colors = listOf(
            0xFF5FCCBB, // Mint
            0xFF64B5F6, // Azul
            0xFF81C784, // Verde
            0xFFFFB74D, // Naranja
            0xFFF06292, // Rosa
            0xFF9575CD, // Púrpura
            0xFF4DB6AC, // Teal
            0xFFFFD54F, // Amarillo
            0xFFE57373, // Rojo claro
            0xFF7986CB, // Índigo
            0xFF4DD0E1, // Cyan
            0xFFAED581, // Verde lima
        )
        return colors[index % colors.size]
    }
}