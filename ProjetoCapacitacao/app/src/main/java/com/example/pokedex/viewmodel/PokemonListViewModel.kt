package com.example.pokedex.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
//import com.example.pokedex.data.modeldados.Pokemon
import com.example.pokedex.data.modeldados.remoto.Responses.Pokemon
import com.example.pokedex.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//Ciclo de vida do app
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {

    fun calcDominantColor(drawable: Drawable, onfinish: (Color) -> Unit){
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate{palette -> palette?.dominantSwatch?.rgb?.let { colorValue ->
            onfinish(Color(colorValue))
        }}
    }


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
                //val response = AppModule.api.getPokemonList(limit = pageSize, offset = currentOffset)
                currentOffset+=pageSize
             //   _pokemonList.value = _pokemonList.value.orEmpty() + response.result
            } catch (e: Exception){
                //depois vou acrescentar o erro que vai aparecer na UI

            } finally {
                _isLoading.value = false
            }

        }
    }




}