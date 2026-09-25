package com.jazc.portfolio.pokemon

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPokemonDetail: GetPokemonDetailUseCase,
) : ViewModel() {
    private val pokemonId = requireNotNull(savedStateHandle.get<Int>(PokemonRoute.Detail.ID))
    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    init { loadPokemon() }

    fun loadPokemon() {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        viewModelScope.launch {
            try {
                val pokemon = getPokemonDetail(pokemonId)
                _uiState.update { it.copy(pokemon = pokemon) }
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

data class PokemonDetailUiState(
    val pokemon: PokemonItem? = null,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
)
