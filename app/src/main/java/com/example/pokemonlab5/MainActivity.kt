package com.example.pokemonlab5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokemonlab5.ui.screens.DetailScreen
import com.example.pokemonlab5.ui.screens.ListScreen

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

                    NavHost(
                        navController = navController,
                        startDestination = "list"
                    ) {
                        composable("list") {
                            ListScreen(
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