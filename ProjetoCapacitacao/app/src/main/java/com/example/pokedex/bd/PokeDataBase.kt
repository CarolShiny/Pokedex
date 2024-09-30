package com.example.pokedex.bd

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [
    FavoritePokemon::class],version = 1, exportSchema = false)
abstract class PokeDataBase: RoomDatabase (){
    abstract fun favoritePokemonDAO(): FavoritePokemonDAO
}