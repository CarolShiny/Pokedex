package com.example.pokedex.data.modeldados.remoto.Responses

data class Ability(
    val ability: com.example.pokedex.data.modeldados.remoto.Responses.AbilityX,
    val is_hidden: Boolean,
    val slot: Int
)