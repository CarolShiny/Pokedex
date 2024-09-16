package com.example.pokedex.main

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pokedex.modeldados.Pokemon
import com.example.pokedex.viewmodel.PokemonViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen (viewModel: PokemonViewModel) {
    //Coletar os estados da View
    val pokemonList by viewModel.pokemonList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    //UI
    Scaffold( topBar = {
        TopAppBar(title = { Text("Pokédex") })
    },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                } else {
                    LazyColumn {
                        items(pokemonList) { pokemon ->
                            PokemonListItem(pokemon = pokemon)
                        }
                    }
                }

                Button(onClick = { viewModel.fetchPokemonList() }) {
                    Text("Carregar mais...")
                }
            }

        }
    )

}

@Composable
fun PokemonListItem(pokemon: Pokemon){
    Text(text = pokemon.name,
        modifier = Modifier.padding(8.dp))
}

