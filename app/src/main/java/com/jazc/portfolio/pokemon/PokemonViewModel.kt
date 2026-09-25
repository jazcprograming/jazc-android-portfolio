package com.jazc.portfolio.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonList: GetPokemonListUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PokemonUiState())
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    private val pageSize = 20
    private var nextOffset = 0

    init { loadNextPage() }

    fun loadNextPage() {
        if (_uiState.value.isLoading || _uiState.value.endReached) return
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        viewModelScope.launch {
            try {
                val page = getPokemonList(offset = nextOffset, limit = pageSize)
                nextOffset += pageSize
                _uiState.update { state ->
                    state.copy(
                        pokemon = state.pokemon + page.pokemon,
                        endReached = nextOffset >= page.totalCount || page.pokemon.isEmpty(),
                    )
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _uiState.update { it.copy(hasError = true) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
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
