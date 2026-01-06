package com.example.composenavdemo.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberLastLocation(onLocation: (Location?) -> Unit) {
    val context = LocalContext.current
    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var location by remember { mutableStateOf<Location?>(null) }

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(permissionsState.allPermissionsGranted) {
        if (permissionsState.allPermissionsGranted) {
            fetchLastLocation(locationClient) { fetchedLocation ->
                location = fetchedLocation
                onLocation(fetchedLocation)
            }
        } else {
            permissionsState.launchMultiplePermissionRequest()
        }
    }
}

@SuppressLint("MissingPermission")
private fun fetchLastLocation(locationClient: com.google.android.gms.location.FusedLocationProviderClient, onLocation: (Location?) -> Unit) {
    locationClient.lastLocation.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            onLocation(task.result)
        } else {
            onLocation(null)
        }
    }
}

/**
 * Devuelve un color basado en el valor del Índice de Calidad del Aire (AQI).
 * La codificación de colores se basa en los estándares de la EPA.
 */
fun getAqiColor(aqi: Int): Color {
    return when (aqi) {
        in 0..50 -> Color(0xFF00E400) // Verde
        in 51..100 -> Color(0xFFFFFF00) // Amarillo
        in 101..150 -> Color(0xFFFF7E00) // Naranja
        in 151..200 -> Color(0xFFFF0000) // Rojo
        in 201..300 -> Color(0xFF8F3F97) // Púrpura
        else -> Color(0xFF7E0023) // Marrón
    }
}

/**
 * Devuelve una etiqueta descriptiva basada en el valor del Índice de Calidad del Aire (AQI).
 */
fun getAqiLabel(aqi: Int): String {
    return when (aqi) {
        in 0..50 -> "Buena"
        in 51..100 -> "Moderada"
        in 101..150 -> "Dañina a la salud para grupos sensibles"
        in 151..200 -> "Dañina a la salud"
        in 201..300 -> "Muy dañina a la salud"
        else -> "Peligrosa"
    }
}