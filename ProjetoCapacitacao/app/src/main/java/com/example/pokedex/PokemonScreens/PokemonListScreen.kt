package com.example.pokedex.PokemonScreens

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.imageLoader
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.data.models.PokedexListEntry
import com.example.pokedex.ui.theme.RobotoCondensed
import com.example.pokedex.viewmodel.PokemonListViewModel


//Tela de lista de Pokemon

@Composable
fun PokemonListScreen (navController: NavController, viewModel: PokemonListViewModel = hiltViewModel()) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ){
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon_Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            SearchBar(hint = "Pesquisar...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                query ->
                viewModel.filterPokemonList(query)  // Atualizar a lista de acordo com a pesquisa
            }
            Spacer(modifier = Modifier.height(16.dp))
            PokemonList(navController = navController)
                
            }
        }
    }


//Barra de pesquisa
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = " ",
    onSearch: (String) -> Unit
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
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
){
    val pokemonList by remember { viewModel.filteredPokemonList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel._loadError }
    val isLoading by remember { viewModel.isLoading }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        //calculo para determinar o itens que são os pokemons
        val itemCount = if (pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        }else {
            pokemonList.size / 2 + 1
        }
        items(itemCount){
            //Verificar a contagem de itens e ver se precisa rolar para baixo
            if(it >= itemCount - 1 && !viewModel.endReached.value && viewModel.filteredPokemonList.value.size == viewModel._pokemonList.size){
                viewModel.loadPokemonPaginated()

            }
            PokedexRow(rowIndex = it, entries = pokemonList, navController = navController)
        }
    }
    Box(  //Mensagem de carregamento ou de erro
        contentAlignment =  Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        if (isLoading){
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        else{
            RetrySection(error = loadError){
                viewModel.loadPokemonPaginated()
            }
        }
    }

}


//vai cuidar da entrada da pokedex
@Composable
fun PokedexEntry(
    entry: PokedexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
){
    val defaultDominatColor = MaterialTheme.colorScheme.surface

    //vai armazenar a cor dominante
    var dominantColor by remember {
        mutableStateOf(defaultDominatColor)
    }

    val context = LocalContext.current   //obtem o contexto

    //Cria uma ImageRequest separado para processamento da cor dominante
    LaunchedEffect(entry.imageUrl) {
        val request = ImageRequest.Builder(context)
            .data(entry.imageUrl)
            .allowHardware(false) // Necessário para operações de Bitmap
            .build()

        val drawable = request.context.imageLoader.execute(request).drawable
        drawable?.let {
            // Calcular a cor dominante
            viewModel.calcDominantColor(it) { color ->
                dominantColor = color
            }
        }
    }

    // Observe o estado reativo do favorito
    //var isFavorite by remember { mutableStateOf(viewModel.isPokemonFavorite(entry.number)) }

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
            .clickable {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 1.dp)
            )
        }
    }


}

@Composable
fun GlideImage(
    imageUrl: String,
    modifier: Modifier = Modifier,

) { //Integração com o compose para carregar as imagens
    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        update = { imageView ->
            Glide.with(imageView.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image) // indica a espera pelo carregamento das imagens
                .error(R.drawable.baseline_sync_problem_24) // Caso ocorra erro no carregamento, aparecerá essa imagem
                .into(imageView)
        },
        modifier = modifier
            .size(120.dp)
    )
}

//torna as linhas da entrada da pokedex em uma linha combinável
@Composable
fun PokedexRow(
    rowIndex: Int,
    entries: List<PokedexListEntry>,
    navController: NavController
){ //Entradas dos Pokemons na Pokedex

    Column {
        Row {
            PokedexEntry(entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f) // atribue a mesma quant de largura para cada pokemon
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (entries.size >= rowIndex * 2 + 2){
                PokedexEntry(entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f))
            } else{
                Spacer(modifier = Modifier.weight(1f))
            }
    }
        Spacer(modifier = Modifier.height(16.dp))

}
}

//Mensagem de carregamento ou de erro
@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(text = error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        ) {
            Text(text = "Tentar novamente")
        }
    }
}




