package com.example.pokedex.api

import retrofit2.http.Query
import com.example.pokedex.modeldados.PokemonResponse
import retrofit2.http.GET

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonResponse
}