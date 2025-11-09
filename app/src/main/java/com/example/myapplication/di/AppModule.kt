package com.example.myapplication.di

import com.example.myapplication.ui.DetailViewModel
import com.example.myapplication.core.data.domain.usecase.GamesInteractor
import com.example.myapplication.core.data.domain.usecase.GamesUseCase
import com.example.myapplication.ui.event.ListGamesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<GamesUseCase> { GamesInteractor(get()) }
}

val viewModelModule = module {
    viewModel { ListGamesViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}