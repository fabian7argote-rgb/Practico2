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

    LaunchedEffect(Unit) {
        vm.obtenerGeneros()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Géneros") })
        }
    ) { padding ->

        when (state) {

            is UiState.Loading -> CircularProgressIndicator()

            is UiState.Error -> Text("Error")

            is UiState.Success -> {
                val generos = (state as UiState.Success<List<Genero>>).data

                LazyColumn(modifier = Modifier.padding(padding)) {

                    items(generos) { genero ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(genero.nombre)

                            Button(onClick = {
                                eliminarId = genero.id
                            }) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

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