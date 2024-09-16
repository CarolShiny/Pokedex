package com.example.pokedex.data.modeldados.remoto.Responses

data class Pokemon(
    val abilities: List<com.example.pokedex.data.modeldados.remoto.Responses.Ability>,
    val base_experience: Int,
    val cries: com.example.pokedex.data.modeldados.remoto.Responses.Cries,
    val forms: List<com.example.pokedex.data.modeldados.remoto.Responses.Form>,
    val game_indices: List<com.example.pokedex.data.modeldados.remoto.Responses.GameIndice>,
    val height: Int,
    val held_items: List<com.example.pokedex.data.modeldados.remoto.Responses.HeldItem>,
    val id: Int,
    val is_default: Boolean,
    val location_area_encounters: String,
    val moves: List<com.example.pokedex.data.modeldados.remoto.Responses.Move>,
    val name: String,
    val order: Int,
    val past_abilities: List<Any?>,
    val past_types: List<Any?>,
    val species: com.example.pokedex.data.modeldados.remoto.Responses.Species,
    val sprites: com.example.pokedex.data.modeldados.remoto.Responses.Sprites,
    val stats: List<com.example.pokedex.data.modeldados.remoto.Responses.Stat>,
    val types: List<com.example.pokedex.data.modeldados.remoto.Responses.Type>,
    val weight: Int
)