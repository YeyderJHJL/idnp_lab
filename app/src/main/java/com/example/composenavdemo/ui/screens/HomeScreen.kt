package com.example.composenavdemo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla principal que permite ingresar un texto y navegar a la segunda pantalla
 * @param onNavigateToDetail Callback para navegar a la pantalla de detalle con el texto
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit
) {
    var textToSend by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pantalla Principal") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título
            Text(
                text = "Enviar Mensaje",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campo de texto
            OutlinedTextField(
                value = textToSend,
                onValueChange = { textToSend = it },
                label = { Text("Ingresa un texto") },
                placeholder = { Text("Escribe algo aquí...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                singleLine = false,
                maxLines = 3
            )

            // Botón de navegación
            Button(
                onClick = {
                    if (textToSend.isNotBlank()) {
                        onNavigateToDetail(textToSend.trim())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = textToSend.isNotBlank()
            ) {
                Text(
                    text = "Ir a Segunda Pantalla",
                    fontSize = 16.sp
                )
            }

            // Texto de ayuda
            if (textToSend.isBlank()) {
                Text(
                    text = "Ingresa un texto para habilitar el botón",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}