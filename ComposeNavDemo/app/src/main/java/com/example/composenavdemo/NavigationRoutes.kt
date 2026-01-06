package com.example.composenavdemo

/**
 * Objeto sellado que define las rutas de navegación de la aplicación
 */
sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Detail : Screen("detail_screen/{text}") {
        fun createRoute(text: String) = "detail_screen/$text"
    }
}