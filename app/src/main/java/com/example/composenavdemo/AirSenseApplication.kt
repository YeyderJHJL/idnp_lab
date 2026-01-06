package com.example.composenavdemo

import android.app.Application
import com.example.composenavdemo.di.AppContainer
import com.example.composenavdemo.di.DefaultAppContainer

/**
 * Clase Application personalizada para la aplicación AirSense.
 * Se encarga de inicializar y mantener el contenedor de dependencias.
 */
class AirSenseApplication : Application() {

    /**
     * Contenedor de dependencias para toda la aplicación.
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Se crea el contenedor de dependencias cuando se inicia la aplicación.
        container = DefaultAppContainer(this)
    }
}
