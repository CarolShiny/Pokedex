package com.example.pokedex.api

import retrofit2.http.Query

import com.example.pokedex.data.modeldados.remoto.Responses.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path

//Configuração do retrofit, os limites que eles vão pegar os nomes dos Pokemon
//vai retornar a lista de Pokemon

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
    )

}