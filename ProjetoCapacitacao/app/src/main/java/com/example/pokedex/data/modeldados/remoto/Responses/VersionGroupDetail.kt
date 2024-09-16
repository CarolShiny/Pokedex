package com.example.pokedex.data.modeldados.remoto.Responses

data class VersionGroupDetail(
    val level_learned_at: Int,
    val move_learn_method: com.example.pokedex.data.modeldados.remoto.Responses.MoveLearnMethod,
    val version_group: com.example.pokedex.data.modeldados.remoto.Responses.VersionGroup
)