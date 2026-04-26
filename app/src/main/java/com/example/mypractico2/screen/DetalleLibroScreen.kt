package com.example.mypractico2.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mypractico2.model.Libro
import com.example.mypractico2.network.RetrofitClient
import com.example.mypractico2.viewmodel.LibroViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleLibroScreen(navController: NavController, id: Int) {

    val vm: LibroViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    var libro by remember { mutableStateOf<Libro?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        val res = RetrofitClient.api.getLibro(id)
        if (res.isSuccessful) libro = res.body()
    }

    libro?.let {

        Column(modifier = Modifier.padding(16.dp)) {

            AsyncImage(
                model = it.imagen,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )

            Text(it.nombre, style = MaterialTheme.typography.headlineSmall)
            Text("Autor: ${it.autor}")
            Text("ISBN: ${it.isbn}")
            Text("Editorial: ${it.editorial}")
            Text("Calificacion: ${it.calificacion}")

            Spacer(modifier = Modifier.height(10.dp))

            Text("Sinopsis:")
            Text(it.sinopsis)

            Spacer(modifier = Modifier.height(10.dp))

            Row {

                Button(onClick = {
                    showDialog = true
                }) {
                    Text("Eliminar")
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(onClick = {
                        vm.eliminarLibro(id)
                        navController.popBackStack()
                    }) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("No")
                    }
                },
                title = { Text("Confirmar") },
                text = { Text("¿Eliminar libro?") }
            )
        }
    }
}