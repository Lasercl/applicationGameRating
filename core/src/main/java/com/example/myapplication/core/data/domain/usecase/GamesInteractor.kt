package com.example.myapplication.core.data.domain.usecase

import com.example.myapplication.core.data.Resource
import com.example.myapplication.core.data.domain.model.Game
import com.example.myapplication.core.repository.IGamesRepository
import kotlinx.coroutines.flow.Flow

class GamesInteractor(private val gameRepository: IGamesRepository): GamesUseCase {

    override fun getAllGames()=gameRepository.getAllGames()

    override fun getFavoriteGames()=gameRepository.getFavoriteGames()

    override fun setFavoriteGame(game: Game, state: Boolean) = gameRepository.setFavoriteGame(game,state)
    override fun getGameDetail(id: Int): Flow<Resource<Game>> = gameRepository.getGameDetail(id)

}