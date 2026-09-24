package com.jazc.portfolio.data.network

import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {
    @GET("pokemon/{nameOrId}")
    suspend fun getPokemon(@Path("nameOrId") nameOrId: String): Pokemon
}

data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
)
