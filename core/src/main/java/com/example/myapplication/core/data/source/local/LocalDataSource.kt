package com.example.myapplication.core.data.source.local

import com.example.myapplication.core.data.source.local.entity.GameEntity
import com.example.myapplication.core.data.source.local.room.GamesDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val gamesDao: GamesDao) {

    companion object {
        private var instance: LocalDataSource? = null

        fun getInstance(gamesDao: GamesDao): LocalDataSource =
            instance ?: synchronized(this) {
                instance ?: LocalDataSource(gamesDao)
            }
    }

    fun getAllGames(): Flow<List<GameEntity>> = gamesDao.getAllGames()

    fun getFavoriteGames(): Flow<List<GameEntity>> =
        gamesDao.getFavoriteGames()

    fun insertGames(gamesList: List<GameEntity>) =
        gamesDao.insertGames(gamesList)

    fun setFavoriteGames(games: GameEntity, newState: Boolean) {
        games.isFavorite = newState
        gamesDao.updateFavoriteGames(games)
    }

    fun getGameById(id: Int): Flow<GameEntity> {
        return gamesDao.getGameById(id)

    }
}