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

// Asumo que estos colores están definidos en tu archivo de tema (p. ej. ui/theme/Color.kt)
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
                onNavigateToDevices = {
                    navController.navigate(AirSenseScreen.Devices.route)
                },
                onNavigateToProfile = {
                    navController.navigate(AirSenseScreen.Profile.route)
                }
            )
        }

        // --- INICIO: Bloque añadido ---
        // Pantalla de Detalles de Estación (faltaba este bloque)
        composable(
            route = AirSenseScreen.StationDetails.route,
            arguments = listOf(navArgument("stationId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Extraer el stationId de los argumentos de la ruta
            val stationId = backStackEntry.arguments?.getString("stationId")
            // TODO: Implementar StationDetailsScreen y pasar el stationId
            PlaceholderScreen(
                title = "Detalles de Estación",
                description = "Mostrando detalles para la estación: $stationId",
                onBack = { navController.popBackStack() }
            )
        }
        // --- FIN: Bloque añadido ---

        // Pantalla de Dispositivos (próximamente)
        composable(route = AirSenseScreen.Devices.route) {
            // TODO: Implementar DevicesScreen
            PlaceholderScreen(
                title = "Dispositivos",
                description = "Lista de Air Purifier y Smart Air Monitor",
                onBack = { navController.popBackStack() }
            )
        }

        // Pantalla de Perfil (próximamente)
        composable(route = AirSenseScreen.Profile.route) {
            // TODO: Implementar ProfileScreen
            PlaceholderScreen(
                title = "Perfil",
                description = "Configuración de usuario",
                onBack = { navController.popBackStack() }
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
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            // Se usa TopAppBar del paquete material3 (no el obsoleto)
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                // Asumo que 'AirSenseMint' está definido en tu tema
                // colors = TopAppBarDefaults.topAppBarColors(
                //     containerColor = AirSenseMint
                // )
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
                    // color = AirSenseDarkText
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    // color = AirSenseLightText
                )
            }
        }
    }
}
