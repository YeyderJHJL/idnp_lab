package com.example.composenavdemo.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla de demostración de animaciones
 * Muestra un círculo que se anima al presionar un botón
 *
 * PRÁCTICA 4: Animaciones
 * - Animación de tamaño (scale)
 * - Animación de color
 * - Animación de rotación
 * - Estado de UI integrado con animaciones
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationScreen(
    onNavigateBack: () -> Unit = {}
) {
    // Estado que controla si el círculo está expandido
    var isExpanded by remember { mutableStateOf(false) }

    // Animación del tamaño (scale)
    val scale by animateFloatAsState(
        targetValue = if (isExpanded) 2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale_animation"
    )

    // Animación del color
    val color by animateColorAsState(
        targetValue = if (isExpanded) Color(0xFFFF6B6B) else AirSenseMint,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearOutSlowInEasing
        ),
        label = "color_animation"
    )

    // Animación de rotación continua (cuando está expandido)
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isExpanded) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_animation"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Animaciones en Compose") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AirSenseMint,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Sección superior: Información
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Demostración de Animaciones",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = AirSenseDarkText
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        AnimationInfoRow(
                            label = "Estado:",
                            value = if (isExpanded) "Expandido" else "Normal"
                        )
                        AnimationInfoRow(
                            label = "Escala:",
                            value = "${String.format("%.2f", scale)}x"
                        )
                        AnimationInfoRow(
                            label = "Rotación:",
                            value = "${rotation.toInt()}°"
                        )
                        AnimationInfoRow(
                            label = "Color:",
                            value = if (isExpanded) "Rojo" else "Mint"
                        )
                    }
                }
            }

            // Sección central: Círculo animado
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Círculo animado
                Canvas(
                    modifier = Modifier
                        .size(150.dp)
                ) {
                    // Aplicar transformaciones
                    rotate(rotation) {
                        drawCircle(
                            color = color,
                            radius = size.minDimension / 2 * scale
                        )
                    }
                }

                // Texto sobre el círculo
                Text(
                    text = "🎨",
                    fontSize = (40 * scale).sp,
                    modifier = Modifier.offset(y = (-10).dp)
                )
            }

            // Sección inferior: Controles
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botón principal de animación
                Button(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AirSenseMint
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = if (isExpanded) "Contraer Círculo" else "Expandir Círculo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón secundario para reset
                OutlinedButton(
                    onClick = { isExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AirSenseMint
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = "Resetear",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Explicación
                Text(
                    text = "Presiona el botón para animar el círculo.\n" +
                            "Observa cómo cambia el tamaño, color y rotación.",
                    fontSize = 12.sp,
                    color = AirSenseLightText,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

/**
 * Componente reutilizable para mostrar información de la animación
 */
@Composable
private fun AnimationInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = AirSenseDarkText
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = AirSenseMint
        )
    }
}