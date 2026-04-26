package com.example.mypractico2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypractico2.model.Genero
import com.example.mypractico2.network.RetrofitClient
import com.example.mypractico2.utils.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GeneroViewModel : ViewModel() {

    private val _generosState = MutableStateFlow<UiState<List<Genero>>>(UiState.Loading)
    val generosState: StateFlow<UiState<List<Genero>>> = _generosState

    fun obtenerGeneros() {
        viewModelScope.launch {
            _generosState.value = UiState.Loading
            try {
                val response = RetrofitClient.api.getGeneros()
                if (response.isSuccessful) {
                    _generosState.value =
                        UiState.Success(response.body() ?: emptyList())
                } else {
                    _generosState.value = UiState.Error("Error en API")
                }
            } catch (e: Exception) {
                _generosState.value =
                    UiState.Error(e.message ?: "Error")
            }
        }
    }

    fun crearGenero(nombre: String) {
        viewModelScope.launch {
            try {
                RetrofitClient.api.crearGenero(Genero(0, nombre))
                obtenerGeneros()
            } catch (e: Exception) {
                _generosState.value = UiState.Error("Error al crear género")
            }
        }
    }

    fun eliminarGenero(id: Int) {
        viewModelScope.launch {
            try {
                RetrofitClient.api.eliminarGenero(id)
                obtenerGeneros()
            } catch (e: Exception) {
                _generosState.value = UiState.Error("Error al eliminar género")
            }
        }
    }
}