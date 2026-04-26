package com.example.mypractico2.model

data class Libro(
    val id: Int,
    val nombre: String,
    val autor: String,
    val editorial: String,
    val imagen: String,
    val sinopsis: String,
    val isbn: String,
    val calificacion: Double
)
