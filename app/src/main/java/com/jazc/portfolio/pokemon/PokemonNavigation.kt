package com.jazc.portfolio.pokemon

import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.navArgument
import com.jazc.portfolio.navigation.AppRoute

fun NavGraphBuilder.pokemonGraph(navController: NavHostController) {
    navigation(startDestination = PokemonRoute.List.route, route = AppRoute.Pokemon.route) {
        composable(PokemonRoute.List.route) { entry ->
            val flowEntry = remember(entry) { navController.getBackStackEntry(AppRoute.Pokemon.route) }
            val viewModel: PokemonViewModel = hiltViewModel(flowEntry)
            PokemonListScreen(
                pokemon = viewModel.pokemon,
                isLoading = viewModel.isLoading,
                hasError = viewModel.hasError,
                onRetry = viewModel::loadPokemon,
                onPokemonClick = { id ->
                    navController.navigate(PokemonRoute.Detail.createRoute(id)) { launchSingleTop = true }
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable(
            route = PokemonRoute.Detail.route,
            arguments = listOf(navArgument(PokemonRoute.Detail.ID) { type = NavType.IntType }),
        ) { entry ->
            val flowEntry = remember(entry) { navController.getBackStackEntry(AppRoute.Pokemon.route) }
            val viewModel: PokemonViewModel = hiltViewModel(flowEntry)
            val id = requireNotNull(entry.arguments).getInt(PokemonRoute.Detail.ID)
            PokemonDetailScreen(
                pokemon = viewModel.pokemon.firstOrNull { it.id == id },
                isLoading = viewModel.isLoading,
                hasError = viewModel.hasError,
                onRetry = viewModel::loadPokemon,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
