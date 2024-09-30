package com.example.pokedex.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokedex.bd.FavoritePokemon
//import com.example.pokedex.data.modeldados.Pokemon
import com.example.pokedex.data.modeldados.remoto.Responses.Pokemon
import com.example.pokedex.data.models.PokedexListEntry
import com.example.pokedex.repository.FavoritePokemonRepository
import com.example.pokedex.repository.PokemonRepository
import com.example.pokedex.util.Constants.PAGE_SIZE
import com.example.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

//Ciclo de vida do app
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository,
    private val favoritePokemonRepository: FavoritePokemonRepository
): ViewModel() {

    //variável para carregar a primeira página
    private var curPage = 0

    var _pokemonList = mutableListOf<PokedexListEntry>()
    val filteredPokemonList = mutableStateOf<List<PokedexListEntry>>(listOf()) // armazenar a lista em tempo real

    var _loadError = mutableStateOf("") //tratamento de erro
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false) //para parar a paginação ao final da lista

    //variável para armazenar uma lista de Pkemons favoritos
    val _favoritePokemonList = MutableStateFlow<List<FavoritePokemon>>(emptyList())
    private var favoritePokemonList: StateFlow<List<FavoritePokemon>> = _favoritePokemonList
    val favoritePokemonFilteredList = mutableStateOf<List<FavoritePokemon>>(listOf())

    init {
        loadPokemonPaginated()
        loadFavorites()
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

        //mostra pokemons com base no que o usuário pesquisar por nome
    fun filterPokemonList(query: String) {
        if (query.isEmpty()) {
            filteredPokemonList.value = _pokemonList // Mostrar todos os pokémons se não houver pesquisa
        } else {
            filteredPokemonList.value = _pokemonList.filter {
                it.pokemonName.lowercase(Locale.ROOT).startsWith(query.lowercase(Locale.ROOT))
            }
        }
    }

    //encontrar pokemons favoritados
    fun filterFavoritePokemonList(query: String){
        if(query.isEmpty()){
            favoritePokemonFilteredList.value = _favoritePokemonList.value
        } else{
            favoritePokemonFilteredList.value = _favoritePokemonList.value.filter {
                it.pokemonName.startsWith(query, ignoreCase = true)
            }
        }
    }

    //carregamento dos pokemons favoritados na página
    fun loadFavorites(){
        viewModelScope.launch (Dispatchers.IO){
            favoritePokemonRepository.getAllFavoritePokemons().collect(){
                _favoritePokemonList.value = it
            }
        }
    }

    fun getFavoritePokemonPokedexListEntries(): StateFlow<List<PokedexListEntry>>{
        return favoritePokemonList.map{
            favoriteList-> favoriteList.map{ favorite->
                PokedexListEntry(
                    pokemonName = favorite.pokemonName,
                    imageUrl = favorite.imageUrl,
                    number = favorite.number
                )
        }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    fun isPokemonFavorite(pokemonId: Int): Boolean{
        return _favoritePokemonList.value.any{
            it.number == pokemonId
        }
    }

    //adicionando pokemons a lista
    fun addPokemonFavorites(pokemon: FavoritePokemon){
        viewModelScope.launch (Dispatchers.IO){
            try{
                favoritePokemonRepository.addFavoritePokemon(pokemon)
                Log.i("Favorite", "Pokemon: ${pokemon.pokemonName}, adicionado com sucesso aos favoritos!")
            }catch (e:Exception){
                Log.e("Favorite","Falha ao favoritar Pokemon",e)

            }
        }
    }

    //remover Pokemons da lista de Pokemons favoritados
    fun removerPokemonFavoritos(pokemon: FavoritePokemon){
        viewModelScope.launch (Dispatchers.IO){
            favoritePokemonRepository.removeFavoritePokemon(pokemon)
        }
    }
}