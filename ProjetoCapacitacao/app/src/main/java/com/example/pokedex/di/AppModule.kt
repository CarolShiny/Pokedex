package com.example.pokedex.di
import com.example.pokedex.api.PokeApiService
import com.example.pokedex.repository.PokemonRepository


// fornece o repositório pokemon para criar a  Pokedex do app

object AppModule {

    fun providePokemonRepository(
        api: PokeApiService
    ) = PokemonRepository(api)

   // Instância única de Retrofit para ser utilizada no app, pega o link do RetrofitInstance

//    fun providePokeApiService(): PokeApiService{
//        return Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create()) // cria e converte o Json em classes de dados
//            .baseUrl(BASE_URL)
//            .build()
//            .create(PokeApiService::class.java)
//    }

}