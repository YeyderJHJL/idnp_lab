package com.example.composenavdemo.navigation

/**
 * Rutas de navegación de AirSense
 */
sealed class AirSenseScreen(val route: String) {
    // Autenticación
    object Login : AirSenseScreen("login")
    object Register : AirSenseScreen("register")

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
}