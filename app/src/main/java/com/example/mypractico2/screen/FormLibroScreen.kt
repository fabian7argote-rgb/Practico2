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
import com.example.mypractico2.network.RetrofitClient
import com.example.mypractico2.utils.UiState
import com.example.mypractico2.utils.Validaciones
import com.example.mypractico2.viewmodel.GeneroViewModel
import com.example.mypractico2.viewmodel.LibroViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormLibroScreen(navController: NavController, id: Int?) {

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
    var error by remember { mutableStateOf("") }
    LaunchedEffect(id) {
        if (id != null) {
            try {
                val res = RetrofitClient.api.getLibro(id)

                if (res.isSuccessful && res.body() != null) {
                    val libro = res.body()!!

                    nombre = libro.nombre ?: ""
                    autor = libro.autor ?: ""
                    editorial = libro.editorial ?: ""
                    imagen = libro.imagen ?: ""
                    sinopsis = libro.sinopsis ?: ""
                    isbn = libro.isbn ?: ""
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(Unit) {
        vmGenero.obtenerGeneros()
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        vmGenero.obtenerGeneros()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

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
                        error = "El nombre está vacío"
                        return@Button
                    }

                    Validaciones.campoVacio(autor) -> {
                        error = "El autor está vacío"
                        return@Button
                    }

                    Validaciones.campoVacio(editorial) -> {
                        error = "La editorial está vacía"
                        return@Button
                    }

                    !Validaciones.esISBNValido(isbn) -> {
                        error = "ISBN inválido"
                        return@Button
                    }

                    !Validaciones.urlValida(imagen) -> {
                        error = "La URL de imagen no es válida"
                        return@Button
                    }

                    seleccionados.isEmpty() -> {
                        error = "Selecciona al menos un género"
                        return@Button
                    }
                }

                error = ""

                if (id == null) {
                    // 🔹 CREAR
                    vmLibro.crearLibroConGeneros(
                        nombre,
                        autor,
                        editorial,
                        imagen,
                        sinopsis,
                        isbn,
                        seleccionados
                    )

                } else {
                    // 🔹 EDITAR
                    val libro = Libro(
                        id,
                        nombre,
                        autor,
                        editorial,
                        imagen,
                        sinopsis,
                        isbn,
                        0.0
                    )

                    vmLibro.actualizarLibro(id, libro)
                }

                navController.popBackStack()
            }
        ) {
            Text("Guardar")
        }
    }


}