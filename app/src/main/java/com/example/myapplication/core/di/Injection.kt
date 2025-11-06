package com.example.myapplication.core.di

import android.content.Context
import com.example.myapplication.retrofit.ApiConfig
import com.example.myapplication.core.data.GamesRepository
import com.example.myapplication.core.data.domain.usecase.GamesInteractor
import com.example.myapplication.core.data.domain.usecase.GamesUseCase
import com.example.myapplication.core.data.source.local.LocalDataSource
import com.example.myapplication.core.data.source.local.room.GamesDatabase
import com.example.myapplication.core.data.source.remote.RemoteDataSource
import com.example.myapplication.core.domain.repository.IGamesRepository
import com.example.myapplication.core.utils.AppExecutors

object Injection {
    private fun provideRepository(context: Context): IGamesRepository {
        val database = GamesDatabase.getInstance(context)
        val apiService = ApiConfig.getApiService()

        val remoteDataSource = RemoteDataSource.getInstance(apiService)
        val localDataSource = LocalDataSource.getInstance(database.GamesDao())
        val appExecutors = AppExecutors()

        return GamesRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }

    fun provideGameUseCase(context: Context): GamesUseCase {
        val repository = provideRepository(context)
        return GamesInteractor(repository)
    }
}
