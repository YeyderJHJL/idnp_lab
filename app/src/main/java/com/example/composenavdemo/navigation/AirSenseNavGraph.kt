package com.example.composenavdemo.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composenavdemo.ui.screens.*

/**
 * Grafo de navegación completo de AirSense
 * Conecta todas las pantallas de la aplicación
 */
@Composable
fun AirSenseNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AirSenseScreen.Login.route
    ) {
        // Pantalla de Login
        composable(route = AirSenseScreen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(AirSenseScreen.Register.route)
                },
                onLoginSuccess = {
                    // Limpiar backstack y navegar al dashboard
                    navController.navigate(AirSenseScreen.Dashboard.route) {
                        popUpTo(AirSenseScreen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Register
        composable(route = AirSenseScreen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    // Registrar y navegar al dashboard
                    navController.navigate(AirSenseScreen.Dashboard.route) {
                        popUpTo(AirSenseScreen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Dashboard Principal
        composable(route = AirSenseScreen.Dashboard.route) {
            DashboardScreen(
                onNavigateToStationDetails = { stationId ->
                    navController.navigate(
                        AirSenseScreen.StationDetails.createRoute(stationId)
                    )
                },
                onNavigateToDevices = {
                    navController.navigate(AirSenseScreen.Devices.route)
                },
                onNavigateToProfile = {
                    navController.navigate(AirSenseScreen.Profile.route)
                }
            )
        }

        // PRÁCTICA 4: Pantalla de Animaciones
        composable(route = AirSenseScreen.Animation.route) {
            AnimationScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Pantalla de Dispositivos (próximamente)
        composable(route = AirSenseScreen.Devices.route) {
            // TODO: Implementar DevicesScreen
            PlaceholderScreen(
                title = "Dispositivos",
                description = "Lista de Air Purifier y Smart Air Monitor",
                onBack = { navController.popBackStack() },
                onNavigateToAnimation = {
                    navController.navigate(AirSenseScreen.Animation.route)
                }
            )
        }

        // Pantalla de Perfil (próximamente)
        composable(route = AirSenseScreen.Profile.route) {
            // TODO: Implementar ProfileScreen
            PlaceholderScreen(
                title = "Perfil",
                description = "Configuración de usuario",
                onBack = { navController.popBackStack() },
                onNavigateToAnimation = {
                    navController.navigate(AirSenseScreen.Animation.route)
                }
            )
        }
    }
}

/**
 * Pantalla placeholder para pantallas pendientes de implementar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaceholderScreen(
    title: String,
    description: String,
    onBack: () -> Unit,
    onNavigateToAnimation: () -> Unit = {}
) {
    androidx.compose.material3.Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { androidx.compose.material3.Text(title) },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = onBack) {
                        androidx.compose.material3.Icon(
                            androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                            "Volver"
                        )
                    }
                },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = AirSenseMint
                )
            )
        }
    ) { padding ->
        androidx.compose.foundation.layout.Box(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            androidx.compose.foundation.layout.Column(
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                androidx.compose.material3.Text(
                    text = title,
                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                    color = AirSenseDarkText
                )
                androidx.compose.foundation.layout.Spacer(
                    modifier = androidx.compose.ui.Modifier.height(16.dp)
                )
                androidx.compose.material3.Text(
                    text = description,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = AirSenseLightText
                )
                androidx.compose.foundation.layout.Spacer(
                    modifier = androidx.compose.ui.Modifier.height(32.dp)
                )
                androidx.compose.material3.Button(
                    onClick = onNavigateToAnimation,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = AirSenseMint
                    )
                ) {
                    androidx.compose.material3.Text("Ver Práctica de Animaciones")
                }
            }
        }
    }
}