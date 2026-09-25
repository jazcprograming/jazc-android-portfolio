package com.jazc.portfolio.pokemon

data class PokemonUiState(
    val pokemon: List<PokemonItem> = emptyList(),
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val endReached: Boolean = false,
)
