package com.jazc.portfolio.pokemon

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor() : ViewModel() {
    val pokemon = List(20) { index ->
        PokemonItem(id = index + 1, name = "Pokemon ${index + 1}")
    }
}

data class PokemonItem(val id: Int, val name: String)
