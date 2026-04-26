package com.example.mypractico2.utils

object Validaciones {

    fun campoVacio(texto: String): Boolean {
        return texto.trim().isEmpty()
    }

    fun esISBNValido(isbn: String): Boolean {
        return isbn.length >= 10
    }

    fun longitudMinima(texto: String, min: Int): Boolean {
        return texto.trim().length >= min
    }

    fun urlValida(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }
}