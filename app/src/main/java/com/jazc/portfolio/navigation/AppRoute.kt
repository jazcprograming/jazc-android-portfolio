package com.jazc.portfolio.navigation

sealed class AppRoute(val route: String) {
    data object Home : AppRoute("home")
    data object DesignSystem : AppRoute("design-system")
    data object PokemonList : AppRoute("pokemon")
    data object PokemonDetail : AppRoute("pokemon/{pokemonId}") {
        const val ID = "pokemonId"
        fun createRoute(id: Int) = "pokemon/$id"
    }
}
