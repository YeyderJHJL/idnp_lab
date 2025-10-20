package com.example.composenavdemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Cloud

/**
 * Pantalla principal (Dashboard) de AirSense
 * Muestra la calidad del aire actual y accesos rápidos
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToStationDetails: (String) -> Unit = {},
    onNavigateToDevices: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Bienvenido",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "Comprueba la calidad del aire hoy.",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Búsqueda */ }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AirSenseMint
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = AirSenseMint
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, "Home") },
                    label = { Text("Home") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.LocationOn, "Ubicaciones") },
                    label = { Text("Ubicaciones") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .offset(y = (-16).dp)
                                .clip(CircleShape)
                                .background(AirSenseMint)
                                .clickable { onNavigateToDevices() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Add,
                                "Agregar",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    },
                    label = { },
                    selected = false,
                    onClick = { onNavigateToDevices() }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, "Notificaciones") },
                    label = { Text("Alertas") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, "Configuración") },
                    label = { Text("Ajustes") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3; onNavigateToProfile() }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Ubicación actual
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = AirSenseMint,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Arequipa, Cercado",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AirSenseDarkText
                    )
                }
                Text(
                    text = "Jueves, 10:00 a.m",
                    fontSize = 12.sp,
                    color = AirSenseLightText
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card principal de calidad del aire
            AirQualityCard(
                aqi = 47,
                quality = "BIEN",
                temperature = "31°C",
                description = "Parcialmente nublado",
                onClick = { onNavigateToStationDetails("station_1") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Título de estaciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ESTACIÓN DNI 1 (Rotonda HI)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AirSenseDarkText
                )
                TextButton(onClick = { /* Ver más */ }) {
                    Text("Ver más", color = AirSenseMint)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Calidad del aire",
                fontSize = 12.sp,
                color = AirSenseLightText
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Métricas de contaminantes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PollutantCard("PM2.5", "29", "μg/m³", Color(0xFF81C784))
                PollutantCard("PM10", "45", "μg/m³", Color(0xFFFFB74D))
                PollutantCard("NO2", "12", "μg/m³", Color(0xFF64B5F6))
            }
        }
    }
}

/**
 * Card principal con AQI y temperatura
 */
@Composable
private fun AirQualityCard(
    aqi: Int,
    quality: String,
    temperature: String,
    description: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lado izquierdo: AQI
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = quality,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF81C784)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = aqi.toString(),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = AirSenseDarkText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "AQI",
                    fontSize = 14.sp,
                    color = AirSenseLightText
                )
            }

            // Lado derecho: Temperatura
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        Icons.Default.Cloud,
                        contentDescription = null,
                        tint = Color(0xFFFFB74D),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = temperature,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = AirSenseDarkText
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = AirSenseLightText
                )
            }
        }
    }
}

/**
 * Card de contaminante individual
 */
@Composable
private fun PollutantCard(
    name: String,
    value: String,
    unit: String,
    color: Color
) {
    Surface(
        modifier = Modifier
            .width(110.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = AirSenseLightText
            )

            Column {
                Text(
                    text = value,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = unit,
                    fontSize = 10.sp,
                    color = AirSenseLightText
                )
            }
        }
    }
}