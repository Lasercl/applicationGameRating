package com.example.myapplication.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.core.data.source.local.entity.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface  GamesDao {
    @Query("SELECT * FROM games")
    fun getAllGames(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games where isFavorite = 1")
    fun getFavoriteGames(): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGames(games: List<GameEntity>)

    @Update
    fun updateFavoriteGames(games: GameEntity)
    @Query("SELECT * FROM games WHERE gamesId = :id")
    fun getGameById(id: Int): Flow<GameEntity>

}