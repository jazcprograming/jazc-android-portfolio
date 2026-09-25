package com.jazc.portfolio.pokemon

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonList: GetPokemonListUseCase,
) : ViewModel() {
    var pokemon by mutableStateOf<List<PokemonItem>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var hasError by mutableStateOf(false)
        private set

    init { loadPokemon() }

    fun loadPokemon() {
        if (isLoading) return
        isLoading = true
        hasError = false
        viewModelScope.launch {
            try {
                pokemon = getPokemonList()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                hasError = true
            } finally {
                isLoading = false
            }
        }
    }
}

data class PokemonItem(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val frontDefault: String?,
)
