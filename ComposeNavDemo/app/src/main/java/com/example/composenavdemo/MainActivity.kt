package com.example.composenavdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.composenavdemo.data.preferences.PreferencesManager
import com.example.composenavdemo.navigation.AirSenseNavGraph
import com.example.composenavdemo.ui.theme.ComposeNavDemoTheme
import com.example.composenavdemo.utils.NotificationUtils

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

        // Crear canal de notificaciones
        NotificationUtils.createNotificationChannel(this)

        setContent {
            val context = LocalContext.current
            val preferencesManager = remember { PreferencesManager(context) }
            val themeMode by preferencesManager.themeMode.collectAsState(initial = PreferencesManager.ThemeMode.SYSTEM)
            val dynamicColor by preferencesManager.dynamicColor.collectAsState(initial = true)

            ComposeNavDemoTheme(
                themeMode = themeMode,
                dynamicColor = dynamicColor
            ) {
                val navController = rememberNavController()
                // Al colocar NavGraph directamente aquí, nos aseguramos de que
                // TODAS las pantallas (incluyendo Login/Register) hereden el tema.
                AirSenseNavGraph(navController = navController)
            }
        }
    }
}