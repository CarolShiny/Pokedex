package com.example.pokedex.data.modeldados.remoto.Responses

import com.google.gson.annotations.SerializedName


data class GenerationI(
    @SerializedName("red-blue")
    val redBlue: RedBlue,
    val yellow: Yellow
)