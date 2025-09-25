package com.example.pokemonlab5.network

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonBasic>
)

// Pokemon nombre y url
data class PokemonBasic(
    val name: String,
    val url: String
) {
    fun getId(): Int {
        return url.split("/").dropLast(1).last().toInt()
    }

    fun getImageUrl(): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${getId()}.png"
    }
}

// Detalles
data class PokemonDetail(
    val id: Int,
    val name: String,
    val sprites: PokemonSprites,
    val height: Int,
    val weight: Int,
    val types: List<PokemonType>
)

// Sprites
data class PokemonSprites(
    val front_default: String?,
    val back_default: String?,
    val front_shiny: String?,
    val back_shiny: String?
)

data class PokemonType(
    val slot: Int,
    val type: PokemonTypeInfo
)

data class PokemonTypeInfo(
    val name: String,
    val url: String
)