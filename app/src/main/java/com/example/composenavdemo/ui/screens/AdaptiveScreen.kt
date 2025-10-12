package com.example.composenavdemo.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Pantalla adaptable que demuestra:
 * - Adaptación automática a tamaños de fuente del sistema
 * - Soporte para modo claro y oscuro
 * - Adaptación a diferentes tamaños de pantalla
 * - Soporte para cambio de orientación
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveScreen() {
    // Obtener configuración actual del dispositivo
    val configuration = LocalConfiguration.current

    // Detectar orientación
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Detectar tipo de dispositivo por ancho de pantalla
    val screenWidth = configuration.screenWidthDp
    val isTablet = screenWidth >= 600

    // Estado para el contador de clicks
    var clickCount by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Interfaz Adaptable",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        // Adaptar layout según orientación
        if (isLandscape) {
            // Layout horizontal para landscape
            LandscapeLayout(
                modifier = Modifier.padding(paddingValues),
                isTablet = isTablet,
                clickCount = clickCount,
                onButtonClick = { clickCount++ }
            )
        } else {
            // Layout vertical para portrait
            PortraitLayout(
                modifier = Modifier.padding(paddingValues),
                isTablet = isTablet,
                clickCount = clickCount,
                onButtonClick = { clickCount++ }
            )
        }
    }
}

/**
 * Layout para orientación vertical (Portrait)
 */
@Composable
private fun PortraitLayout(
    modifier: Modifier = Modifier,
    isTablet: Boolean,
    clickCount: Int,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(if (isTablet) 48.dp else 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ContentSection(
            isTablet = isTablet,
            clickCount = clickCount,
            onButtonClick = onButtonClick
        )
    }
}

/**
 * Layout para orientación horizontal (Landscape)
 */
@Composable
private fun LandscapeLayout(
    modifier: Modifier = Modifier,
    isTablet: Boolean,
    clickCount: Int,
    onButtonClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(if (isTablet) 48.dp else 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContentSection(
            isTablet = isTablet,
            clickCount = clickCount,
            onButtonClick = onButtonClick
        )
    }
}

/**
 * Sección de contenido reutilizable
 */
@Composable
private fun ContentSection(
    isTablet: Boolean,
    clickCount: Int,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Información del dispositivo
        DeviceInfoCard(isTablet = isTablet)

        Spacer(modifier = Modifier.height(32.dp))

        // Título principal
        Text(
            text = "Pantalla Adaptable con Jetpack Compose",
            style = if (isTablet) {
                MaterialTheme.typography.headlineLarge
            } else {
                MaterialTheme.typography.headlineMedium
            },
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción
        Text(
            text = "Esta interfaz se adapta automáticamente a:\n" +
                    "• Tamaño de fuente del sistema\n" +
                    "• Modo claro y oscuro\n" +
                    "• Diferentes tamaños de pantalla\n" +
                    "• Orientación del dispositivo",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Contador de clicks
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Has presionado el botón $clickCount ${if (clickCount == 1) "vez" else "veces"}",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón principal
        Button(
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isTablet) 64.dp else 56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Presionar",
                style = if (isTablet) {
                    MaterialTheme.typography.titleLarge
                } else {
                    MaterialTheme.typography.titleMedium
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón secundario
        OutlinedButton(
            onClick = { /* Acción secundaria */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isTablet) 64.dp else 56.dp)
        ) {
            Text(
                text = "Acción Secundaria",
                style = if (isTablet) {
                    MaterialTheme.typography.titleLarge
                } else {
                    MaterialTheme.typography.titleMedium
                }
            )
        }
    }
}

/**
 * Card con información del dispositivo actual
 */
@Composable
private fun DeviceInfoCard(isTablet: Boolean) {
    val configuration = LocalConfiguration.current
    val orientation = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        "Horizontal (Landscape)"
    } else {
        "Vertical (Portrait)"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Información del Dispositivo",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "• Tipo: ${if (isTablet) "Tableta" else "Teléfono"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• Ancho: ${configuration.screenWidthDp} dp",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• Alto: ${configuration.screenHeightDp} dp",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• Orientación: $orientation",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• Densidad: ${configuration.densityDpi} dpi",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}