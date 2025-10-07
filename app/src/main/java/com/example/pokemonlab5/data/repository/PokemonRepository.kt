package com.example.pokemonlab5.data.repository

import com.example.pokemonlab5.data.network.PokeApiService
import com.example.pokemonlab5.data.network.PokemonBasic
import com.example.pokemonlab5.data.network.PokemonDetail

class PokemonRepository(private val apiService: PokeApiService) {

    //Para obtener la lista de pokemones de la API
    suspend fun getPokemonList(limit: Int = 100): List<PokemonBasic> {
        return try {
            val response = apiService.getPokemonList(limit = limit)
            response.results
        } catch (e: Exception) {
            throw Exception("Error al cargar la lista de Pokémon: ${e.message}")
        }
    }

    //Para obtener los detalles de pokemon
    suspend fun getPokemonDetail(id: Int): PokemonDetail {
        return try {
            apiService.getPokemonDetail(id)
        } catch (e: Exception) {
            throw Exception("Error al cargar los detalles del Pokémon: ${e.message}")
        }
    }
}