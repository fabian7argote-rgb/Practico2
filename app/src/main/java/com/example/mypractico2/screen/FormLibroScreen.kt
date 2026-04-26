package com.example.mypractico2.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mypractico2.model.Genero
import com.example.mypractico2.model.Libro
import com.example.mypractico2.utils.UiState
import com.example.mypractico2.utils.Validaciones
import com.example.mypractico2.viewmodel.GeneroViewModel
import com.example.mypractico2.viewmodel.LibroViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormLibroScreen(navController: NavController) {

    val vmLibro: LibroViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val vmGenero: GeneroViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    var nombre by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var editorial by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }
    var sinopsis by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }

    val generosState by vmGenero.generosState.collectAsState()
    val seleccionados = remember { mutableStateListOf<Int>() }

    LaunchedEffect(Unit) {
        vmGenero.obtenerGeneros()
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        vmGenero.obtenerGeneros()
    }

    Column(modifier = Modifier.padding(16.dp)) {

        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") })
        OutlinedTextField(autor, { autor = it }, label = { Text("Autor") })
        OutlinedTextField(editorial, { editorial = it }, label = { Text("Editorial") })
        OutlinedTextField(imagen, { imagen = it }, label = { Text("Imagen") })
        OutlinedTextField(sinopsis, { sinopsis = it }, label = { Text("Sinopsis") })
        OutlinedTextField(isbn, { isbn = it }, label = { Text("ISBN") })

        Spacer(modifier = Modifier.height(10.dp))

        Text("Géneros:")

        if (generosState is UiState.Success) {
            val lista = (generosState as UiState.Success<List<Genero>>).data

            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ){
                items(lista) { genero ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Checkbox(
                            checked = seleccionados.contains(genero.id),
                            onCheckedChange = {
                                if (it) seleccionados.add(genero.id)
                                else seleccionados.remove(genero.id)
                            }
                        )

                        Text(
                            genero.nombre,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                when {
                    Validaciones.campoVacio(nombre) -> {
                        println("Nombre vacío")
                        return@Button
                    }

                    Validaciones.campoVacio(autor) -> {
                        println("Autor vacío")
                        return@Button
                    }

                    Validaciones.campoVacio(editorial) -> {
                        println("Editorial vacío")
                        return@Button
                    }

                    !Validaciones.esISBNValido(isbn) -> {
                        println("ISBN inválido")
                        return@Button
                    }

                    !Validaciones.urlValida(imagen) -> {
                        println("URL inválida")
                        return@Button
                    }

                    seleccionados.isEmpty() -> {
                        println("Selecciona al menos un género")
                        return@Button
                    }
                }
            val libro = Libro(
                0, nombre, autor, editorial,
                imagen, sinopsis, isbn, 0.0
            )

            vmLibro.crearLibroConGeneros(libro, seleccionados)
            navController.popBackStack()
        }
        ) {
            Text("Guardar")
        }
    }
}