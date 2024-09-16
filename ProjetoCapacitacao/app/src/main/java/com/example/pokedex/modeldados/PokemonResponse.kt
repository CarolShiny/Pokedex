package com.example.pokedex.modeldados

//modelo de como os dados serão armazenados
data class PokemonResponse (
    val result: List<Pokemon>
)

data class Pokemon(
    val name: String,
    val url: String
)

