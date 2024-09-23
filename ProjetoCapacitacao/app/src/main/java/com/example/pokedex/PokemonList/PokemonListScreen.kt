package com.example.pokedex.PokemonList

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.pokedex.R
import com.example.pokedex.data.models.PokedexListEntry
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.ui.theme.RobotoCondensed
import com.example.pokedex.viewmodel.PokemonListViewModel


//Tela de lista de Pokemon

@Composable
fun PokemonListScreen (navController: NavController) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ){
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

@Composable
fun PokedexEntry(
    entry: PokedexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
){
    val defaultDominatColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominatColor)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominatColor
                    )
                )
            )
            .clickable{
                navController.navigate(
                    "pokemon_tela_detalhes/${dominantColor.toArgb()}/${entry.pokemonName}"
                )
            }
    ){
        Column{
            GlideImage(
                imageUrl = entry.imageUrl,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Text(
                text = entry.pokemonName,
                fontFamily = RobotoCondensed,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }


}

@Composable
fun GlideImage(
    imageUrl: String,
    modifier: Modifier = Modifier,

) {
    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        update = { imageView ->
            // Responsável por carregar a imagem
            Glide.with(imageView.context)
                .load(imageUrl)
                //.placeholder(R.drawable.placeholder_image) // Placeholder enquanto carrega
                //.error(R.drawable.error_image) // Imagem de erro caso falhe o carregamento
                .into(imageView)
        },
        modifier = modifier
            .size(120.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPokemonListScreen() {
    val navController = rememberNavController()
    PokedexTheme {
        PokemonListScreen(navController = navController)
    }
}



