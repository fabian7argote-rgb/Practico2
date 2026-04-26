package com.example.mypractico2.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mypractico2.model.Libro
import com.example.mypractico2.utils.UiState
import com.example.mypractico2.viewmodel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaLibrosScreen(navController: NavController) {

    val vm: LibroViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val state by vm.librosState.collectAsState()

    LaunchedEffect(Unit) {
        vm.obtenerLibros()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Biblioteca ") },
                actions = {
                    TextButton(onClick = {
                        navController.navigate("listaGeneros")
                    }) {
                        Text("Géneros")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("formLibro")
            }) {
                Text("+")
            }
        }
    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {

            when (state) {

                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UiState.Error -> {
                    Text("Error al cargar", modifier = Modifier.align(Alignment.Center))
                }

                is UiState.Success -> {
                    val libros = (state as UiState.Success<List<Libro>>).data

                    LazyColumn {
                        items(libros) { libro ->

                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate("detalle/${libro.id}")
                                    }
                            ) {
                                Row(modifier = Modifier.padding(10.dp)) {

                                    AsyncImage(
                                        model = libro.imagen,
                                        contentDescription = null,
                                        modifier = Modifier.size(80.dp)
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column {
                                        Text(libro.nombre, style = MaterialTheme.typography.titleMedium)
                                        Text("Autor: ${libro.autor}")
                                        Text("Editorial: ${libro.editorial}")
                                        Text(" ${libro.calificacion}")
                                        Text(
                                            libro.sinopsis.take(50) + "...",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
 }