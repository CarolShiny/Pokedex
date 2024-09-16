package com.example.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.api.RetrofitInstance
import com.example.pokedex.modeldados.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Response

//Ciclo de vida do app

class PokemonViewModel : ViewModel() {
    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> get() = _pokemonList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    //lógica da paginação para o app poder carregar os Pokemon em blocos
    private var currentOffset = 0  // número de pokemon que já foram carregados
    private val pageSize = 20 // quant Pokemon serão carregados a cada requisição

    fun fetchPokemonList(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getPokemonList(limit = pageSize, offset = currentOffset)
                currentOffset+=pageSize
                _pokemonList.value = _pokemonList.value.orEmpty() + response.result
            } catch (e: Exception){
                //depois vou acrescentar o erro que vai aparecer na UI

            } finally {
                _isLoading.value = false
            }

        }
    }




}