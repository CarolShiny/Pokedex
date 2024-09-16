package com.example.pokedex.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//classe que vai acessar a api

object RetrofitInstance {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    // Instância única de Retrofit para ser utilizada no app
    val api: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApiService::class.java)
    }
}
