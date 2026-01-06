package com.example.composenavdemo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.composenavdemo.AirSenseApplication
import com.example.composenavdemo.ui.screens.*
import com.example.composenavdemo.ui.viewmodel.AddStationViewModel
import com.example.composenavdemo.ui.viewmodel.StationDetailsViewModel
import com.example.composenavdemo.ui.viewmodel.StationsListViewModel

@Composable
fun AirSenseNavGraph(navController: NavHostController) {
    val application = LocalContext.current.applicationContext as AirSenseApplication
    val repository = application.container.airQualityRepository

    NavHost(navController = navController, startDestination = AirSenseScreen.Login.route) {
        composable(AirSenseScreen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(AirSenseScreen.Dashboard.route) {
                    popUpTo(AirSenseScreen.Login.route) { inclusive = true }
                }},
                onNavigateToRegister = { navController.navigate(AirSenseScreen.Register.route) }
            )
        }
        composable(AirSenseScreen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(AirSenseScreen.Dashboard.route) {
                    popUpTo(AirSenseScreen.Register.route) { inclusive = true }
                }},
                onNavigateToLogin = { navController.navigate(AirSenseScreen.Login.route) }
            )
        }
        composable(AirSenseScreen.Dashboard.route) {
            DashboardScreen(
                onNavigateToStationsList = { navController.navigate(AirSenseScreen.StationsList.route) },
                onStationClick = { stationId ->
                    navController.navigate(AirSenseScreen.StationDetails.createRoute(stationId))
                },
                onNavigateToSettings = { navController.navigate(AirSenseScreen.Settings.route) }
            )
        }
        composable(AirSenseScreen.StationsList.route) {
            val stationsListViewModel: StationsListViewModel = viewModel(
                factory = StationsListViewModel.provideFactory(repository)
            )
            StationsListScreen(
                viewModel = stationsListViewModel,
                onStationClick = { stationId ->
                    navController.navigate(AirSenseScreen.StationDetails.createRoute(stationId))
                },
                onNavigateToStationsList = { navController.navigate(AirSenseScreen.StationsList.route) },
                onNavigateToAddStation = { navController.navigate(AirSenseScreen.AddStation.route) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = AirSenseScreen.StationDetails.route,
            arguments = listOf(navArgument("stationId") { type = NavType.LongType })
        ) {
            val stationDetailsViewModel: StationDetailsViewModel = viewModel(
                factory = StationDetailsViewModel.provideFactory(repository)
            )
            StationDetailsScreen(
                viewModel = stationDetailsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(AirSenseScreen.AddStation.route) {
            val addStationViewModel: AddStationViewModel = viewModel(
                factory = AddStationViewModel.provideFactory(repository)
            )
            AddStationScreen(
                viewModel = addStationViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(AirSenseScreen.Settings.route) {
            val preferencesManager = application.container.preferencesManager
            SettingsScreen(
                preferencesManager = preferencesManager,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
