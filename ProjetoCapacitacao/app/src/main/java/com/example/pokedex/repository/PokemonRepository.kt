package com.example.pokedex.repository

import com.example.pokedex.api.PokeApiService
import com.example.pokedex.data.modeldados.remoto.Responses.Pokemon
import com.example.pokedex.data.modeldados.remoto.Responses.PokemonList
import com.example.pokedex.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import jakarta.inject.Inject

//vai armazenar os dados de acordo com o tempo de vida da activity
@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api: PokeApiService
){
    //implementação real da API
    suspend fun  getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        val response = try {
            api.getPokemonList(limit,offset)
        }catch (e:Exception){
            return Resource.Error("Erro no carregamento da página. Verifique sua conecção com a internet ")
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon>{
        val response = try{
            api.getPokemonInfo(pokemonName)
        }catch (e: Exception){
            return Resource.Error("Erro em carregar as informações do Pokemon")
        }
        return Resource.Success(response)
    }
}