package com.example.pokedex.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
//import com.example.pokedex.data.modeldados.Pokemon
import com.example.pokedex.data.modeldados.remoto.Responses.Pokemon
import com.example.pokedex.data.models.PokedexListEntry
import com.example.pokedex.repository.PokemonRepository
import com.example.pokedex.util.Constants.PAGE_SIZE
import com.example.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

//Ciclo de vida do app
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {

    //variável para carregar a primeira página
    private var curPage = 0

    var _pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf()) // armazenar a lista em tempo real

    var _loadError = mutableStateOf("") //tratamento de erro
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false) //para parar a paginação ao final da lista

    init {
        loadPokemonPaginated()
    }


    //Trata a paginação
    fun loadPokemonPaginated(){
        viewModelScope.launch {
            isLoading.value = true

            val result = repository.getPokemonList(PAGE_SIZE,curPage * PAGE_SIZE)

            when (result) {
                is Resource.Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.count

                    val pokedexEntries = result.data.results.mapIndexed { index, entry ->
                        val number = if (entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() } //verificação da url para pegar a imagem
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokedexListEntry(entry.name.capitalize(Locale.ROOT), url, number.toInt())
                    }

                    curPage++

                    _loadError.value = ""
                    isLoading.value = false
                    _pokemonList.value += pokedexEntries  //lista com os pokemons recem-paginados

                }

                is Resource.Error -> {
                    _loadError.value = result.message!!
                    isLoading.value = false
                }

                is Resource.Loading -> TODO()
            }


        }

    }




    fun calcDominantColor(drawable: Drawable, onfinish: (Color) -> Unit){
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate{palette -> palette?.dominantSwatch?.rgb?.let { colorValue ->
            onfinish(Color(colorValue))
        }}
    }
/* *********************************** Fim da alteração ******************************************************************  */

//    //private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
//    //val pokemonList: StateFlow<List<Pokemon>> get() = _pokemonList
//
//    private val _isLoading = MutableStateFlow(false)
//    //val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//
//    //lógica da paginação para o app poder carregar os Pokemon em blocos
//    private var currentOffset = 0  // número de pokemon que já foram carregados
//    private val pageSize = 20 // quant Pokemon serão carregados a cada requisição
//
//    fun fetchPokemonList(){
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                //val response = AppModule.api.getPokemonList(limit = pageSize, offset = currentOffset)
//                currentOffset+=pageSize
//             //   _pokemonList.value = _pokemonList.value.orEmpty() + response.result
//            } catch (e: Exception){
//                //depois vou acrescentar o erro que vai aparecer na UI
//
//            } finally {
//                _isLoading.value = false
//            }
//
//        }
    //}




}