package com.example.composenavdemo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.composenavdemo.Screen
import com.example.composenavdemo.ui.screens.DetailScreen
import com.example.composenavdemo.ui.screens.HomeScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Grafo de navegación que define las pantallas y las transiciones entre ellas
 * @param navController Controlador de navegación
 */
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Pantalla Principal
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToDetail = { text ->
                    // Codificar el texto para evitar problemas con caracteres especiales
                    val encodedText = URLEncoder.encode(
                        text,
                        StandardCharsets.UTF_8.toString()
                    )
                    navController.navigate(Screen.Detail.createRoute(encodedText))
                }
            )
        }

        // Pantalla de Detalle
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("text") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val encodedText = backStackEntry.arguments?.getString("text")
            // Decodificar el texto
            val decodedText = encodedText?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }

            DetailScreen(
                text = decodedText,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}