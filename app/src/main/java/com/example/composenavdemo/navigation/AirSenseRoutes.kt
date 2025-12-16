package com.example.composenavdemo.navigation

/**
 * Rutas de navegación de AirSense
 */
sealed class AirSenseScreen(val route: String) {
    // Menú de prácticas
    object PracticeMenu : AirSenseScreen("practice_menu")

    // PRÁCTICA 2: Adaptabilidad
    object Adaptive : AirSenseScreen("adaptive")

    // PRÁCTICA 3: Autenticación y Formularios
    object Login : AirSenseScreen("login")
    object Register : AirSenseScreen("register")

    // PRÁCTICA 4: Animaciones
    object Animation : AirSenseScreen("animation")

    // PRÁCTICA 5: Listas Dinámicas
    object StationsList : AirSenseScreen("stations_list")

    // Pantallas principales
    object Dashboard : AirSenseScreen("dashboard")
    object StationDetails : AirSenseScreen("station_details/{stationId}") {
        fun createRoute(stationId: String) = "station_details/$stationId"
    }
    object Devices : AirSenseScreen("devices")
    object DeviceDetails : AirSenseScreen("device_details/{deviceId}") {
        fun createRoute(deviceId: String) = "device_details/$deviceId"
    }
    object AddDevice : AirSenseScreen("add_device")
    object WiFiSetup : AirSenseScreen("wifi_setup")
    object Profile : AirSenseScreen("profile")
    object Settings : AirSenseScreen("settings")
}