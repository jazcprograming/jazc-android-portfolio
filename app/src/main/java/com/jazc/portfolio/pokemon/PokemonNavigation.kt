package com.jazc.portfolio.pokemon

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            PokemonListScreen(
                uiState = state,
                onLoadMore = viewModel::loadNextPage,
                onPokemonClick = { id ->
                    navController.navigate(PokemonRoute.Detail.createRoute(id)) { launchSingleTop = true }
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable(
            route = PokemonRoute.Detail.route,
            arguments = listOf(navArgument(PokemonRoute.Detail.ID) { type = NavType.IntType }),
        ) {
            val viewModel: PokemonDetailViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val onBack: () -> Unit = {
                if (!navController.popBackStack()) {
                    navController.navigate(AppRoute.Home.route) { launchSingleTop = true }
                }
            }
            BackHandler(onBack = onBack)
            PokemonDetailScreen(
                pokemon = state.pokemon,
                isLoading = state.isLoading,
                hasError = state.hasError,
                onRetry = viewModel::loadPokemon,
                onBack = onBack,
            )
        }
    }
}
