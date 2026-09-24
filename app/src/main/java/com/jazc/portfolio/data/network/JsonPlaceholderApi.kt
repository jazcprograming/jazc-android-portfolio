package com.jazc.portfolio.data.network

import retrofit2.http.GET
import retrofit2.http.Path

interface JsonPlaceholderApi {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): Post
}

data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String,
)
