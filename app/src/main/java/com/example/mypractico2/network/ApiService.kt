package com.example.mypractico2.network


import com.example.mypractico2.model.Genero
import com.example.mypractico2.model.Libro
import com.example.mypractico2.model.LibroGeneroRequest
import com.example.mypractico2.model.LibroRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    //libros

    @GET("libros")
    suspend fun getLibros(): Response<List<Libro>>

    @GET("libros/{id}")
    suspend fun getLibro(
        @Path("id") id: Int
    ): Response<Libro>

    @POST("libros")
    suspend fun crearLibro(
        @Body libro: LibroRequest
    ): Response<Libro>

    @PUT("libros/{id}")
    suspend fun actualizarLibro(
        @Path("id") id: Int,
        @Body libro: Libro
    ): Response<Libro>
    @DELETE("libros/{id}")
    suspend fun eliminarLibro(
        @Path("id") id: Int
    ): Response<Unit>


    // generos
    @GET("generos")
    suspend fun getGeneros(): Response<List<Genero>>

    @POST("generos")
    suspend fun crearGenero(
        @Body genero: Genero
    ): Response<Genero>

    @DELETE("generos/{id}")
    suspend fun eliminarGenero(
        @Path("id") id: Int
    ): Response<Unit>


    // libro-genero

    @POST("libro-generos")
    suspend fun agregarGeneroALibro(
        @Body request: LibroGeneroRequest
    ): Response<Unit>
}