package com.example.pokedex.bd

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_pokemon")
data class FavoritePokemon(
    @PrimaryKey val number: Int,
    val pokemonName: String,
    val imageUrl: String
)