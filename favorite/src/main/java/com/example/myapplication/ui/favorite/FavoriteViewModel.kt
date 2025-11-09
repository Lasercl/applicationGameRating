package com.example.myapplication.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

import com.example.myapplication.core.data.domain.usecase.GamesUseCase

class FavoriteViewModel(gamesUseCase: GamesUseCase) : ViewModel(){
    val game=gamesUseCase.getFavoriteGames().asLiveData()

}
