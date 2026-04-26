package com.example.mypractico2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.*
import com.example.mypractico2.screen.DetalleLibroScreen
import com.example.mypractico2.screen.FormLibroScreen
import com.example.mypractico2.screen.ListaGenerosScreen
import com.example.mypractico2.screen.ListaLibrosScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(navController, startDestination = "listaLibros") {

                composable("listaLibros") {
                    ListaLibrosScreen(navController)
                }

                composable("detalle/{id}") {
                    val id = it.arguments?.getString("id")!!.toInt()
                    DetalleLibroScreen(navController, id)
                }

                composable("formLibro") {
                    FormLibroScreen(navController)
                }

                composable("listaGeneros") {
                    ListaGenerosScreen(navController)
                }
            }
        }
    }
}