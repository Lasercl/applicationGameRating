package com.example.myapplication.ui.event

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.myapplication.retrofit.ApiConfig
import com.example.myapplication.core.data.GamesRepository
import com.example.myapplication.core.data.domain.model.Game
import com.example.myapplication.core.data.domain.usecase.GamesUseCase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListGamesViewModel(gamesUseCase: GamesUseCase) : ViewModel() {
    val game=gamesUseCase.getAllGames().asLiveData()
}