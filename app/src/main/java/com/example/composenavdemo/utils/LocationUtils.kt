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
