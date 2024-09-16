package com.example.pokedex.data.modeldados

//dados pegos da API
data class PokemonResponses (
    val result: List<Pokemon>
)

data class Pokemon(
    val name: String,
    val url: String
)

