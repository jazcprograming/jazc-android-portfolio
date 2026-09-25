package com.jazc.portfolio.pokemon

sealed class PokemonRoute(val route: String) {
    data object List : PokemonRoute("pokemon/list")
    data object Detail : PokemonRoute("pokemon/detail/{pokemonId}") {
        const val ID = "pokemonId"
        fun createRoute(id: Int) = "pokemon/detail/$id"
    }
}
