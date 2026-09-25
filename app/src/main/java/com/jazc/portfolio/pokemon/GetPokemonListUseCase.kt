package com.jazc.portfolio.pokemon

import com.jazc.portfolio.data.network.PokeApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(private val api: PokeApi) {
    suspend operator fun invoke(): List<PokemonItem> = coroutineScope {
        val response = api.getPokemonList(limit = 20, offset = 0)

        response.results.map { item ->
            async {
                val detail = api.getPokemon(item.name)

                PokemonItem(
                    id = detail.id,
                    name = detail.name,
                    height = detail.height,
                    weight = detail.weight,
                    types = detail.types
                        .sortedBy { it.slot }
                        .map { it.type.name },
                    frontDefault = detail.sprites.other.home.frontDefault
                )
            }
        }.awaitAll()
    }
}
