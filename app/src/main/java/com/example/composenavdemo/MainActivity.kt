package com.example.composenavdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.composenavdemo.ui.theme.ComposeNavDemoTheme
import androidx.navigation.compose.rememberNavController
import com.example.composenavdemo.navigation.AirSenseNavGraph

/**
 * Actividad principal que demuestra adaptabilidad en Jetpack Compose
 * - Soporta modo claro y oscuro automáticamente
 * - Se adapta a diferentes tamaños de pantalla
 * - Respeta la configuración de accesibilidad del sistema
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // El tema se adapta automáticamente al modo del sistema
            ComposeNavDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AirSenseNavGraph(navController = navController)
                }
            }
        }
    }
}