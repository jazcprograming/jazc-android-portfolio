package com.jazc.portfolio.pokemon

import com.jazc.portfolio.data.network.PokeApi
import com.jazc.portfolio.data.network.PokemonDetailDTO
import javax.inject.Inject

class GetPokemonDetailUseCase @Inject constructor(private val api: PokeApi) {
    suspend operator fun invoke(id: Int): PokemonItem = api.getPokemon(id.toString()).toPokemonItem()
}

internal fun PokemonDetailDTO.toPokemonItem() = PokemonItem(
    id = id,
    name = name.toDisplayName(),
    height = height,
    weight = weight,
    types = types.sortedBy { it.slot }.map { it.type.name.toDisplayName() },
    frontDefault = sprites.other.home.frontDefault,
)

private fun String.toDisplayName(): String =
    split("-").joinToString(" ") { part ->
        part.replaceFirstChar { it.uppercase() }
    }
