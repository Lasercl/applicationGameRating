package com.example.myapplication.retrofit

import com.example.myapplication.core.data.source.remote.response.GameResponse
import com.example.myapplication.core.data.source.remote.response.GamesDetailResponse
import retrofit2.http.*

interface ApiService {
    @GET("games/{id}")
    suspend fun getGameDetail(@Path("id") id: Int): GamesDetailResponse

    @GET("games")
    suspend fun getGames(): GameResponse
}

