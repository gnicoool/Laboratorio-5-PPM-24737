package com.example.pokemonlab5.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokemonlab5.data.repository.PokemonRepository
import com.example.pokemonlab5.data.network.RetrofitClient
import com.example.pokemonlab5.ui.screens.DetailScreen
import com.example.pokemonlab5.ui.screens.ListScreen
import com.example.pokemonlab5.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val repository = remember {
                        PokemonRepository(RetrofitClient.apiService)
                    }
                    val viewModel = remember {
                        MainViewModel(repository)
                    }

                    NavHost(
                        navController = navController,
                        startDestination = "list"
                    ) {
                        composable("list") {
                            ListScreen(
                                viewModel = viewModel,
                                onPokemonClick = { id ->
                                    navController.navigate("detail/$id")
                                }
                            )
                        }

                        composable(
                            "detail/{id}",
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.IntType
                                    defaultValue = 1
                                }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id") ?: 1
                            DetailScreen(
                                viewModel = viewModel,
                                pokemonId = id,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}