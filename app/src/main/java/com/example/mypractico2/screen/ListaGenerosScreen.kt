package com.example.mypractico2.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mypractico2.model.Genero
import com.example.mypractico2.utils.UiState
import com.example.mypractico2.viewmodel.GeneroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaGenerosScreen(navController: NavController) {

    val vm: GeneroViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val state by vm.generosState.collectAsState()

    var eliminarId by remember { mutableStateOf<Int?>(null) }

    //  (crear género)
    var nombreGenero by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        vm.obtenerGeneros()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Géneros") })
        }
    ) { padding ->

        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)) {

            // FORMULARIO CREAR GENERO
            OutlinedTextField(
                value = nombreGenero,
                onValueChange = { nombreGenero = it },
                label = { Text("Nuevo género") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    if (nombreGenero.isNotEmpty()) {
                        vm.crearGenero(nombreGenero)
                        nombreGenero = "" // limpiar campo
                    }

                }
            ) {
                Text("Crear género")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // LISTA
            when (state) {

                is UiState.Loading -> CircularProgressIndicator()

                is UiState.Error -> Text("Error al cargar")

                is UiState.Success -> {
                    val generos = (state as UiState.Success<List<Genero>>).data

                    LazyColumn {

                        items(generos) { genero ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Text(genero.nombre)

                                    Button(
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.error
                                        ),
                                        onClick = {
                                            eliminarId = genero.id
                                        }
                                    ) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // CONFIRMAR ELIMINAR
    eliminarId?.let { id ->
        AlertDialog(
            onDismissRequest = { eliminarId = null },
            confirmButton = {
                Button(onClick = {
                    vm.eliminarGenero(id)
                    eliminarId = null
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                Button(onClick = { eliminarId = null }) {
                    Text("No")
                }
            },
            title = { Text("Confirmar") },
            text = { Text("¿Eliminar género?") }
        )
    }
}