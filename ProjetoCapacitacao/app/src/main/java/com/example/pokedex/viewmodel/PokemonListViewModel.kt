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

    var _pokemonList = mutableListOf<PokedexListEntry>()
    val filteredPokemonList = mutableStateOf<List<PokedexListEntry>>(listOf()) // armazenar a lista em tempo real

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

                    _loadError.value = " "
                    isLoading.value = false

                    _pokemonList.addAll(pokedexEntries)
                    filteredPokemonList.value = _pokemonList  //lista com os pokemons recem-paginados

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
        }
        }
    }

        //mostra pokemons com base no que o usuário pesquisar
    fun filterPokemonList(query: String) {
        if (query.isEmpty()) {
            filteredPokemonList.value = _pokemonList // Mostrar todos os pokémons se não houver pesquisa
        } else {
            filteredPokemonList.value = _pokemonList.filter {
                it.pokemonName.lowercase(Locale.ROOT).startsWith(query.lowercase(Locale.ROOT))
            }
        }
    }





}