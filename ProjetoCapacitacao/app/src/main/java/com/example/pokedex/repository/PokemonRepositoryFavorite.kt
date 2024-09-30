package com.example.pokedex.repository

import com.example.pokedex.bd.FavoritePokemon
import com.example.pokedex.bd.FavoritePokemonDAO
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityScoped
class FavoritePokemonRepository @Inject constructor(
    private val favoritePokemonDao: FavoritePokemonDAO
) {

    fun addFavoritePokemon(pokemon: FavoritePokemon) {
        favoritePokemonDao.insertFavoritePokemon(pokemon)
    }

    fun removeFavoritePokemon(pokemon: FavoritePokemon) {
        favoritePokemonDao.deleteFavoritePokemon(pokemon)
    }

    fun getAllFavoritePokemons(): Flow<List<FavoritePokemon>> = favoritePokemonDao.getAllFavoritePokemon()
}