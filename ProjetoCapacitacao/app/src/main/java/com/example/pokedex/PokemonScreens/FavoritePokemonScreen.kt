package com.example.pokedex.PokemonScreens

import android.annotation.SuppressLint
import android.widget.Space
import android.widget.Toast
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
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pokedex.viewmodel.PokemonListViewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Index
import androidx.room.util.query
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.example.pokedex.R
import com.example.pokedex.bd.FavoritePokemon
import com.example.pokedex.data.modeldados.remoto.Responses.PokemonList
import com.example.pokedex.data.models.PokedexListEntry
import com.example.pokedex.ui.theme.RobotoCondensed


@Composable
 fun FavoritePokemonScreen (
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
 ) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    )
    {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon", //Logo
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "voltar",
                        tint = Color.White,
                        modifier = Modifier
                            .size(52.dp)
                            .clickable {
                                navController.popBackStack() // ação de clique para voltar
                            }
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))

                SearchBar(
                    hint = "Pesquisar: ",
                    modifier = Modifier
                        .weight(1f)
                        .height(16.dp),
                    onSearch = { query ->
                        viewModel.filterFavoritePokemonList(query)
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            PokemonList(navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = " ",
    onSearch: (String) -> Unit,
    text: String
) {
    var isHintDisplayed by remember { mutableStateOf(text.isEmpty()) }

    Box(modifier = Modifier ){
        TextField(
            value = text,
            onValueChange = {
            onSearch(it)
            isHintDisplayed = it.isEmpty()
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
        if(isHintDisplayed){
                Text(
                    text = hint,
                    color = Color.Blue,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    )
                )
            }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    //observa a lista de favoritos
    val favoritePokemonList by viewModel.getFavoritePokemonPokedexListEntries().collectAsState(initial = emptyList())

    if(favoritePokemonList.isEmpty()){
        Box(modifier = Modifier.fillMaxSize()){
            Text(text = "Lista vazia")
        }
    } else{
        LazyColumn (contentPadding = PaddingValues(16.dp)) {
            val itemCount = if(favoritePokemonList.size % 2 == 0){
                favoritePokemonList.size /2
            } else{
                favoritePokemonList.size / 2+1
            }
            items(itemCount){
                index -> if (index >= itemCount -1 && !viewModel.endReached.value && viewModel.favoritePokemonFilteredList.value.size == viewModel._favoritePokemonList.value.size){
                    viewModel.loadFavorites()
                }
                PokedexRow(rowIndex = index, entries = favoritePokemonList, navController = navController )
            }
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
    val defaultDominantColor = MaterialTheme.colorScheme.surface

    //vai armazenar a cor dominante
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    val context = LocalContext.current   //obtem o contexto
    
    //Processa a cor dominante
    LaunchedEffect(entry.imageUrl) {
        val request = ImageRequest.Builder(context)
            .data(entry.imageUrl)
            .allowHardware(false)
            .build()
        
        val drawable = request.context.imageLoader.execute(request).drawable
        drawable?.let { 
            viewModel.calcDominantColor(it){
                color -> dominantColor = color
            }
        }
        
    }
    
    //estado de favoritado
    
    var isFavorite by remember {
        mutableStateOf(viewModel.isPokemonFavorite(entry.number))
    }
    
    Box(modifier = modifier
        .shadow(5.dp, RoundedCornerShape(10.dp))
        .clip(RoundedCornerShape(10.dp))
        .aspectRatio(1f)
        .background(
            Brush.verticalGradient(
                listOf(
                    dominantColor,
                    defaultDominantColor
                )
            )
        )
        .clickable { navController.navigate("pokemon_detail_screen/${dominantColor.toArgb()}/${entry.pokemonName}") }
    ) 
    {
        Column {
            AsyncImage(model = entry.imageUrl, contentDescription = entry.pokemonName,
                modifier = modifier
                    .size(100.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
            Text(
                text = entry.pokemonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 1.dp)
            )
            
            IconToggleButton(checked = isFavorite, onCheckedChange = {
                viewModel.removerPokemonFavoritos(
                    FavoritePokemon(
                        entry.number,
                        entry.pokemonName,
                        entry.imageUrl
                    )
                )
                isFavorite = !isFavorite
                Toast.makeText(
                    context, "Pokemon ${entry.pokemonName} removido. ",
                    Toast.LENGTH_SHORT
                ).show()               
                
            } else{
                viewModel.addPokemonFavorites(
                    FavoritePokemon(
                        entry.number,
                        entry.pokemonName,
                        entry.imageUrl
                    )
                )
                isFavorite = !isFavorite
                Toast.makeText(
                    context,"${entry.pokemonName} favoritado.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
                
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favoritar Pokemon",
                    tint = if (isFavorite) colorResource(id = R.color.pink) else colorResource(id = R.color.teal_700)                    
                )        
        
            }
            
        }
    }  
}
        
@Composable
fun PokedexRow(
    rowIndex: Int,
    entries: List<PokedexListEntry>,
    navController: NavController
){
    Column {
        Row {
            PokedexEntry(entry = entries[rowIndex *2], navController = navController,
                modifier = Modifier.weight(1f))
            
            Spacer(modifier = Modifier.width(16.dp))
            if (entries.size >= rowIndex * 2 + 2){
                PokedexEntry(entry = entries[rowIndex *2 + 1], navController = navController,
                    modifier = Modifier.weight(1f))
            } else{
                Spacer(modifier = Modifier.weight(1f))
            }
            
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
    
}