package com.example.composenavdemo.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.composenavdemo.data.preferences.PreferencesManager
import com.example.composenavdemo.ui.screens.AnimationScreen
import com.example.composenavdemo.ui.screens.DashboardScreen
import com.example.composenavdemo.ui.screens.LoginScreen
import com.example.composenavdemo.ui.screens.RegisterScreen
import com.example.composenavdemo.ui.screens.SettingsScreen
import com.example.composenavdemo.ui.screens.StationDetailsScreen
import com.example.composenavdemo.ui.screens.StationsListScreen

/**
 * Grafo de navegación completo de AirSense
 * Conecta todas las pantallas de la aplicación
 */
@Composable
fun AirSenseNavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

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
                },
                onNavigateToSettings = {
                    navController.navigate(AirSenseScreen.Settings.route)
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

        composable(
            route = AirSenseScreen.StationDetails.route,
            arguments = listOf(navArgument("stationId") { type = NavType.StringType })
        ) {
            val stationId = it.arguments?.getString("stationId")
            if (stationId != null) {
                StationDetailsScreen(
                    stationId = stationId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        composable(route = AirSenseScreen.Settings.route) {
            SettingsScreen(
                preferencesManager = preferencesManager,
                onNavigateBack = { navController.popBackStack() }
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(
                    modifier = Modifier.height(32.dp)
                )
                Button(
                    onClick = onNavigateToAnimation,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Ver Práctica de Animaciones")
                }
            }
        }
    }
}
