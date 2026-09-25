package com.jazc.portfolio.data.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int,
                               @Query("offset") offset: Int): PokemonListDTO

    @GET("pokemon/{nameOrId}")
    suspend fun getPokemon(@Path("nameOrId") nameOrId: String): PokemonDetailDTO
}

data class PokemonDetailDTO(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<TypeObjectDTO>,
    val sprites: SpriteDTO
)

data class TypeObjectDTO(
    val slot: Int,
    val type: TypeDTO
)

data class TypeDTO(
    val name: String
)

data class PokemonListDTO(
    val count: Int,
    val results: List<PokemonItemDTO>
)

data class SpriteDTO(
    val other: PrincipalSpritesDTO
)

data class PrincipalSpritesDTO(
    val home: SpritesLinkDTO
)

data class SpritesLinkDTO(
    @SerializedName("front_default")
    val frontDefault: String
)

data class PokemonItemDTO(
    val name: String,
    val url: String
)
