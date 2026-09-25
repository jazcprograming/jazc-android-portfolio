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
                api.getPokemon(item.name).toPokemonItem()
            }
        }.awaitAll()
        PokemonPage(pokemon = pokemon, totalCount = response.count)
    }

}

data class PokemonPage(val pokemon: List<PokemonItem>, val totalCount: Int)
