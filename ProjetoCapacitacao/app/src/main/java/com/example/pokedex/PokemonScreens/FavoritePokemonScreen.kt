package com.example.pokedex.PokemonScreens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pokedex.viewmodel.PokemonListViewModel

@Composable
 fun FavoritePokemonScreen (
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
 ) {
}