package com.example.pokedex.data.modeldados.remoto.Responses

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)