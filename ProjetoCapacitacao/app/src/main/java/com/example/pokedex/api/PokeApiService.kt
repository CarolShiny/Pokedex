package com.example.pokedex.api

import com.example.pokedex.data.modeldados.remoto.Responses.Pokemon
import retrofit2.http.Query

import com.example.pokedex.data.modeldados.remoto.Responses.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET ("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonList

    //retorna a info do Pokemon
    @GET("pokemon/{name}")
    suspend fun  getPokemonInfo(
        @Path("name") name:String
    ): Pokemon

}