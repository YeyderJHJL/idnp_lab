package com.example.composenavdemo.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.composenavdemo.ui.screens.*
// import com.example.composenavdemo.ui.theme.AirSenseDarkText
// import com.example.composenavdemo.ui.theme.AirSenseLightText
// import com.example.composenavdemo.ui.theme.AirSenseMint

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
                // Añade el nuevo callback para navegar a la lista
                onNavigateToStationsList = {
                    navController.navigate(AirSenseScreen.StationsList.route)
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

        // PRÁCTICA 5: LISTA DE ESTACIONES
        composable(route = AirSenseScreen.StationsList.route) {
            StationsListScreen(
                onNavigateBack = {
                    // Acción para volver a la pantalla anterior (Dashboard)
                    navController.popBackStack()
                },
                onStationClick = { station ->
                    // Navegar a la pantalla de detalles usando el ID de la estación
                    navController.navigate(
                        AirSenseScreen.StationDetails.createRoute(station.id)
                    )
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