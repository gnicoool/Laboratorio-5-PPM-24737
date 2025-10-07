package com.example.pokemonlab5.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonlab5.data.repository.PokemonRepository
import com.example.pokemonlab5.data.network.PokemonBasic
import com.example.pokemonlab5.data.network.PokemonDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
//Estados para ListScreen
sealed class PokemonListUiState {
    object Loading : PokemonListUiState()
    data class Success(val pokemonList: List<PokemonBasic>) : PokemonListUiState()
    data class Error(val message: String) : PokemonListUiState()
}

//Estados para DetailScreen
sealed class PokemonDetailUiState {
    object Loading : PokemonDetailUiState()
    data class Success(val pokemon: PokemonDetail) : PokemonDetailUiState()
    data class Error(val message: String) : PokemonDetailUiState()
}

class MainViewModel(private val repository: PokemonRepository) : ViewModel() {

    // StateFlow para la lista de pokemones
    private val _pokemonListState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Loading)
    val pokemonListState: StateFlow<PokemonListUiState> = _pokemonListState.asStateFlow()

    // StateFlow para los detalles del pokemon
    private val _pokemonDetailState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)
    val pokemonDetailState: StateFlow<PokemonDetailUiState> = _pokemonDetailState.asStateFlow()

    init {
        loadPokemonList()
    }

    //Cargar lista de pokemones desde Repository
    fun loadPokemonList() {
        viewModelScope.launch {
            _pokemonListState.value = PokemonListUiState.Loading
            try {
                val pokemonList = repository.getPokemonList(limit = 100)
                _pokemonListState.value = PokemonListUiState.Success(pokemonList)
            } catch (e: Exception) {
                _pokemonListState.value = PokemonListUiState.Error(
                    e.message ?: "Error desconocido al cargar Pokémon"
                )
            }
        }
    }

    //Cargar detalles de un pokemon
    fun loadPokemonDetail(id: Int) {
        viewModelScope.launch {
            _pokemonDetailState.value = PokemonDetailUiState.Loading
            try {
                val pokemon = repository.getPokemonDetail(id)
                _pokemonDetailState.value = PokemonDetailUiState.Success(pokemon)
            } catch (e: Exception) {
                _pokemonDetailState.value = PokemonDetailUiState.Error(
                    e.message ?: "Error desconocido al cargar el detalle"
                )
            }
        }
    }
}