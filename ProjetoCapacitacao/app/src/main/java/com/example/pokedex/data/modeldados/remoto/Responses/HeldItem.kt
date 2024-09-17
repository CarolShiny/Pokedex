package com.example.pokedex.data.modeldados.remoto.Responses

data class HeldItem(
    val item: com.example.pokedex.data.modeldados.remoto.Responses.Item,
    val version_details: List<com.example.pokedex.data.modeldados.remoto.Responses.VersionDetail>
)