package com.example.pokedex.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.modeldados.remoto.Responses.Pokemon
import com.example.pokedex.repository.PokemonRepository
import com.example.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        var result: Resource<Pokemon> = Resource.Loading()
        viewModelScope.launch {
            result = repository.getPokemonInfo(pokemonName)
        }
        return result
    }


// Estado para armazenar os detalhes do Pokémon
    //var pokemonDetail = mutableStateOf<Pokemon?>(null)
//    var loadError = mutableStateOf("") // Estado de erro
//    var isLoading = mutableStateOf(false) // Indicador de carregamento

    // Função para carregar os detalhes do Pokémon
//    fun loadPokemonDetail(pokemonName: String) {
//        viewModelScope.launch {
//            isLoading.value = true
//
//            // Chama o método do repositório para obter os detalhes do Pokémon
//            val result = repository.getPokemonInfo(pokemonName)
//
//            when (result) {
//                is Resource.Success -> {
//                    pokemonDetail.value = result.data
//                    loadError.value = ""
//                }
//
//                is Resource.Error -> {
//                    loadError.value = result.message ?: "Erro desconhecido"
//                }
//
//                is Resource.Loading -> {
//                    isLoading.value = true
//                }
//            }
//
//            isLoading.value = false
//        }
    }



