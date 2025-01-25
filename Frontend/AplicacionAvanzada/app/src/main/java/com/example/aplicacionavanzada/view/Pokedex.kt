package com.example.aplicacionavanzada.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.aplicacionavanzada.R
import com.example.aplicacionavanzada.model.pokedex.PokemonData
import com.example.aplicacionavanzada.viewmodel.pokedex.ViewModelPokemon

@Composable
fun Pokedex(viewModel: ViewModelPokemon, navController: NavController) {
    val pokemonList by viewModel.pokemonList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchPokemons()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        if (pokemonList.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            PokedexList(pokemonList)
        }
    }
}

@Composable
fun PokedexList(pokemonList: List<PokemonData>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentPadding = PaddingValues(top = 32.dp)
    ) {
        items(pokemonList) { pokemon ->
            PokedexItem(pokemon)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PokedexItem(pokemon: PokemonData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .border(2.dp, color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        // Imagen del Pokémon
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pokemon.img.replace("http://", "https://"))
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.placeholder),
            contentDescription = pokemon.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.CenterVertically)
                .padding(end = 16.dp)
        )

        // Datos del Pokémon
        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            // Número y Nombre
            Text(
                text = "#${pokemon.num} ${pokemon.name}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Tipos
            Text(
                text = "${stringResource(R.string.types)} ${pokemon.type.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Debilidades
            Text(
                text = "${stringResource(R.string.weaknesses)} ${pokemon.weaknesses.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


