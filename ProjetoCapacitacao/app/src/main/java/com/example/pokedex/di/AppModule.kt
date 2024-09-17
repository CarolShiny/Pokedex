package com.example.pokedex.di

import com.example.pokedex.api.PokeApiService
import com.example.pokedex.api.RetrofitInstance.BASE_URL
import com.example.pokedex.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// fornece o repositório pokemon para criar a  Pokedex do app
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokeApiService
    ) = PokemonRepository(api)

   // Instância única de Retrofit para ser utilizada no app, pega o link do RetrofitInstance
   @Singleton
   @Provides
    fun providePokeApiService(): PokeApiService{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()) // cria e converte o Json em classes de dados
            .baseUrl(BASE_URL)
            .build()
            .create(PokeApiService::class.java)
    }

}