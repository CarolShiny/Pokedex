package com.example.pokedex.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.pokedex.ui.theme.PokedexTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex.PokemonList.PokemonListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {
                // Controlador de Navegação do app
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "pokemon_list_screen"){
                    composable("pokemon_list_screen"){
                        PokemonListScreen(navController = navController)

                    }
                    composable("pokemon_tela_detalhes/{dominantColor}/{PokemonName}",
                        arguments = listOf(
                            navArgument("dominantColor"){
                                type = NavType.IntType //mudar cor de acordo com o tipo de pokemon
                            }, navArgument("pokemonName"){
                                type = NavType.StringType
                            }
                        )
                    ){
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) }?: Color.White
                        }
                        val PokemonName = remember{
                            it.arguments?.getString("pokemonName")
                        }

                    }
                }

            }
        }
    }
}



