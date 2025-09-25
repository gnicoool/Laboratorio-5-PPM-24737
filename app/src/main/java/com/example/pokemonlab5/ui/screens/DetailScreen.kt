package com.example.pokemonlab5.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.pokemonlab5.network.PokemonDetail
import com.example.pokemonlab5.network.RetrofitClient
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    pokemonId: Int,
    onBack: () -> Unit = {}
) {
    var pokemon by remember { mutableStateOf<PokemonDetail?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun loadPokemonDetail() {
        scope.launch {
            isLoading = true
            error = null
            try {
                val response = RetrofitClient.apiService.getPokemonDetail(pokemonId)
                pokemon = response
            } catch (e: Exception) {
                error = "Error al cargar detalles: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    // Llamada a la API
    LaunchedEffect(pokemonId) {
        loadPokemonDetail()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        pokemon?.name?.replaceFirstChar { it.uppercase() } ?: "Pokémon",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (error != null) {
                        IconButton(onClick = { loadPokemonDetail() }) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Reintentar",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFCB05),
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.White
                )
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
                        Text("Cargando detalles...", color = Color.Gray)
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
                            onClick = { loadPokemonDetail() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF003A70)
                            )
                        ) {
                            Text("Reintentar", color = Color.White)
                        }
                    }
                }

                pokemon != null -> {
                    DetailContent(pokemon = pokemon!!)
                }
            }
        }
    }
}

@Composable
fun DetailContent(pokemon: PokemonDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF3D7DCA)),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "#${pokemon.id.toString().padStart(3, '0')}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF003A70)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoItem(label = "Altura", value = "${pokemon.height / 10.0} m")
                    InfoItem(label = "Peso", value = "${pokemon.weight / 10.0} kg")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tipos:",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                pokemon.types.forEach { type ->
                    Text(
                        text ="• ${type.type.name.replaceFirstChar { it.uppercase() }}",
                        color = Color(0xFF003A70)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sprites
        Text(
            text = "Normal",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF003A70)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SpriteCard(
                imageUrl = pokemon.sprites.front_default,
                title = "Front"
            )
            SpriteCard(
                imageUrl = pokemon.sprites.back_default,
                title = "Back"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Shiny",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF003A70)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SpriteCard(
                imageUrl = pokemon.sprites.front_shiny,
                title = "Front Shiny"
            )
            SpriteCard(
                imageUrl = pokemon.sprites.back_shiny,
                title = "Back Shiny"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF003A70)
        )
    }
}

@Composable
fun SpriteCard(
    imageUrl: String?,
    title: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.size(140.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF3D7DCA)),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = title,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Text(
                        text = "Sin imagen",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = Color(0xFF003A70)

        )
    }
}

@Preview(showBackground = true, name = "SpriteCard Preview")
@Composable
fun SpriteCardPreview() {
    SpriteCard(
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
        title = "Front"
    )
}


