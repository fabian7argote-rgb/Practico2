package com.example.mypractico2.model

data class LibroRequest(
    val nombre: String,
    val autor: String,
    val editorial: String,
    val imagen: String,
    val sinopsis: String,
    val isbn: String,
    val calificacion: Double
)