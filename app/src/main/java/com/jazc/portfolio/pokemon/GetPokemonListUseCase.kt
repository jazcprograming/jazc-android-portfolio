package com.jazc.portfolio.pokemon

import com.jazc.portfolio.data.network.PokeApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(private val api: PokeApi) {
    suspend operator fun invoke(offset: Int, limit: Int = 20): PokemonPage = coroutineScope {
        val response = api.getPokemonList(limit = limit, offset = offset)

        val pokemon = response.results.map { item ->
            async {
                val detail = api.getPokemon(item.name)

                PokemonItem(
                    id = detail.id,
                    name = detail.name.toDisplayName(),
                    height = detail.height,
                    weight = detail.weight,
                    types = detail.types
                        .sortedBy { it.slot }
                        .map { it.type.name.toDisplayName() },
                    frontDefault = detail.sprites.other.home.frontDefault
                )
            }
        }.awaitAll()
        PokemonPage(pokemon = pokemon, totalCount = response.count)
    }

    private fun String.toDisplayName(): String =
        split("-").joinToString(" ") { part ->
            part.replaceFirstChar { c->c.uppercase() }
        }
}

data class PokemonPage(val pokemon: List<PokemonItem>, val totalCount: Int)
