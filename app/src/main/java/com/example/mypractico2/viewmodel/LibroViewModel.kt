package com.example.mypractico2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypractico2.model.Libro
import com.example.mypractico2.model.LibroGeneroRequest
import com.example.mypractico2.model.LibroRequest
import com.example.mypractico2.network.RetrofitClient
import com.example.mypractico2.utils.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LibroViewModel : ViewModel() {

    private val _librosState = MutableStateFlow<UiState<List<Libro>>>(UiState.Loading)
    val librosState: StateFlow<UiState<List<Libro>>> = _librosState

    fun obtenerLibros() {
        viewModelScope.launch {
            _librosState.value = UiState.Loading
            try {
                val response = RetrofitClient.api.getLibros()
                if (response.isSuccessful) {
                    _librosState.value =
                        UiState.Success(response.body() ?: emptyList())
                } else {
                    _librosState.value = UiState.Error("Error en API")
                }
            } catch (e: Exception) {
                _librosState.value =
                    UiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun eliminarLibro(id: Int) {
        viewModelScope.launch {
            try {
                RetrofitClient.api.eliminarLibro(id)
                obtenerLibros()
            } catch (e: Exception) {
                _librosState.value = UiState.Error("Error al eliminar")
            }
        }
    }

    fun crearLibroConGeneros(nombre: String,
                             autor: String,
                             editorial: String,
                             imagen: String,
                             sinopsis: String,
                             isbn: String,
                             generos: List<Int>) {

        viewModelScope.launch {
            try {
                val request = LibroRequest(
                    nombre,
                    autor,
                    editorial,
                    imagen,
                    sinopsis,
                    isbn,
                    0.0
                )

                val response = RetrofitClient.api.crearLibro(request)

                if (response.isSuccessful) {
                    obtenerLibros()
                } else {
                    println(response.errorBody()?.string())
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun actualizarLibro(id: Int, libro: Libro) {
        viewModelScope.launch {
            try {
                RetrofitClient.api.actualizarLibro(id, libro)
                obtenerLibros()
            } catch (e: Exception) {
                _librosState.value = UiState.Error("Error al actualizar")
            }
        }
    }
}