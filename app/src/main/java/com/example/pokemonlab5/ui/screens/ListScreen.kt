package com.example.pokemonlab5.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokemonlab5.network.PokemonBasic
import com.example.pokemonlab5.network.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    onPokemonClick: (Int) -> Unit
) {
    var pokemonList by remember { mutableStateOf<List<PokemonBasic>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun loadPokemon() {
        scope.launch {
            isLoading = true
            error = null
            try {
                val response = RetrofitClient.apiService.getPokemonList(limit = 100)
                pokemonList = response.results
            } catch (e: Exception) {
                error = "Error al cargar Pokémon: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadPokemon()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pokemon",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFCB05),
                    titleContentColor = Color.Black
                ),
                actions = {
                    if (error != null) {
                        IconButton(onClick = { loadPokemon() }) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Reintentar",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color(0xFFCC0000))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Cargando Pokémon...", color = Color.Gray)
                    }
                }

                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error!!,
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(
                            onClick = { loadPokemon() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFCC0000)
                            )
                        ) {
                            Text("Reintentar", color = Color.White)
                        }
                    }
                }

                pokemonList.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(pokemonList) { pokemon ->
                            PokemonCard(
                                pokemon = pokemon,
                                onClick = { onPokemonClick(pokemon.getId()) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PokemonCard(
    pokemon: PokemonBasic,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3D7DCA)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = pokemon.getImageUrl(),
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "PokemonCard Preview")
@Composable
fun PokemonCardPreview() {
    val samplePokemon = PokemonBasic(
        name = "pikachu",
        url = "https://pokeapi.co/api/v2/pokemon/25/"
    )
    PokemonCard(
        pokemon = samplePokemon,
        onClick = {}
    )
}