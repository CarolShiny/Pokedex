package com.example.pokedex.data.modeldados.remoto.Responses

data class Move(
    val move: com.example.pokedex.data.modeldados.remoto.Responses.MoveX,
    val version_group_details: List<com.example.pokedex.data.modeldados.remoto.Responses.VersionGroupDetail>
)