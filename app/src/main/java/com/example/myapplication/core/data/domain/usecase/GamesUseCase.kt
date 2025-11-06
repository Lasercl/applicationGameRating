package com.example.myapplication.core.data.domain.usecase

import androidx.lifecycle.LiveData
import com.example.myapplication.core.data.Resource
import com.example.myapplication.core.data.domain.model.Game
import com.example.myapplication.core.data.source.remote.response.GamesDetailResponse
import kotlinx.coroutines.flow.Flow

interface GamesUseCase {
    fun getAllGames(): Flow<Resource<List<Game>>>
    fun getFavoriteGames(): Flow<List<Game>>
    fun setFavoriteGame(game: Game, state: Boolean)
    fun getGameDetail(id: Int): Flow<Resource<Game>>
}