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
    var uiState by mutableStateOf(PokemonUiState())
        private set

    private val pageSize = 20
    private var nextOffset = 0

    init { loadNextPage() }

    fun loadNextPage() {
        if (uiState.isLoading || uiState.endReached) return
        uiState = uiState.copy(isLoading = true, hasError = false)
        viewModelScope.launch {
            try {
                val page = getPokemonList(offset = nextOffset, limit = pageSize)
                nextOffset += pageSize
                uiState = uiState.copy(
                    pokemon = uiState.pokemon + page.pokemon,
                    endReached = nextOffset >= page.totalCount || page.pokemon.isEmpty(),
                )
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                uiState = uiState.copy(hasError = true)
            } finally {
                uiState = uiState.copy(isLoading = false)
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
