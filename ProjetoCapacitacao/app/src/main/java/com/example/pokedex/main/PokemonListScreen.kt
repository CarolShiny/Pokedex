package com.example.pokedex.main


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pokedex.R
import com.example.pokedex.ui.theme.PokedexTheme


//Tela de lista de Pokemon
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen (navController: NavController) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ){
        Greeting(name = "vai")
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            SearchBar(hint = "Pesquisar...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){

            }
                
            }
        }
    }


//Barra de pesquisa
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Nome do Pokemon",
    onSearch: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf(" ") }
    var isHintDisplayed by remember { mutableStateOf(hint != "") }

    Box(modifier = modifier) {
        TextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged { focusState ->
                    isHintDisplayed = !focusState.isFocused && text.isEmpty()
                }

        )

        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.Gray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewPokemonListScreen() {
    val navController = rememberNavController()
    PokedexTheme {
        PokemonListScreen(navController = navController)
    }
}

@Composable
fun Greeting(name:String){
    Text(text = "Hola $name")
}
@Preview(showBackground = true)
@Composable
fun PreviewGreeting(){
    PokedexTheme {
        Greeting("vai")
    }
}

