package com.example.myapplication

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.core.data.domain.model.Game
import com.example.myapplication.core.data.domain.usecase.GamesUseCase

class DetailViewModel(private val gamesUseCase: GamesUseCase) : ViewModel() {
    fun setFavoriteGames(game: Game, newStatus:Boolean) =
        gamesUseCase.setFavoriteGame(game,newStatus)
    fun getGameDetail(id: Int) = gamesUseCase.getGameDetail(id)



}