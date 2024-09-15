package com.example.pokedex.modeldados

data class PokemonResponse (
    val result: List<Pokemon>
)

data class Pokemon(
    val name: String,
    val url: String
)

