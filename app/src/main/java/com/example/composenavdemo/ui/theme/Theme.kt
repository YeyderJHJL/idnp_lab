package com.example.composenavdemo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema de colores para modo oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF004A77),
    onPrimaryContainer = Color(0xFFCCE5FF),

    secondary = Color(0xFF81C784),
    onSecondary = Color(0xFF003A03),
    secondaryContainer = Color(0xFF005306),
    onSecondaryContainer = Color(0xFFA5D6A7),

    tertiary = Color(0xFFFFB74D),
    onTertiary = Color(0xFF4A2800),
    tertiaryContainer = Color(0xFF6A3C00),
    onTertiaryContainer = Color(0xFFFFDDB3),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE2E2E6),
    surface = Color(0xFF1A1C1E),
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = Color(0xFF42474E),
    onSurfaceVariant = Color(0xFFC2C7CF)
)

// Esquema de colores para modo claro
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1976D2),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD1E4FF),
    onPrimaryContainer = Color(0xFF001D36),

    secondary = Color(0xFF388E3C),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFB9F6CA),
    onSecondaryContainer = Color(0xFF002204),

    tertiary = Color(0xFFF57C00),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDDB3),
    onTertiaryContainer = Color(0xFF2B1700),

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    background = Color(0xFFFDFCFF),
    onBackground = Color(0xFF1A1C1E),
    surface = Color(0xFFFDFCFF),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFDFE2EB),
    onSurfaceVariant = Color(0xFF42474E)
)

/**
 * Tema de la aplicación que soporta:
 * - Modo claro y oscuro automático
 * - Colores dinámicos en Android 12+ (Material You)
 * - Tipografía escalable según accesibilidad
 *
 * @param darkTheme Indica si se debe usar el tema oscuro (por defecto usa la configuración del sistema)
 * @param dynamicColor Habilita colores dinámicos en Android 12+ (por defecto true)
 * @param content Contenido de la aplicación
 */
@Composable
fun ComposeNavDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // En Android 12+ (API 31+), usar colores dinámicos si está habilitado
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Usar tema oscuro personalizado
        darkTheme -> DarkColorScheme
        // Usar tema claro personalizado
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}