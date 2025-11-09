package com.example.myapplication.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.myapplication.core.data.domain.usecase.GamesUseCase

class ListGamesViewModel(gamesUseCase: GamesUseCase) : ViewModel() {
    val game=gamesUseCase.getAllGames().asLiveData()
}