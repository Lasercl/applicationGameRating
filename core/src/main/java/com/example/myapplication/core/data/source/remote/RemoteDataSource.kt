package com.example.myapplication.core.data.source.remote

import com.example.myapplication.retrofit.ApiService
import com.example.myapplication.core.data.source.remote.network.ApiResponse
import com.example.myapplication.core.data.source.remote.response.GamesDetailResponse
import com.example.myapplication.core.data.source.remote.response.ResultsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class RemoteDataSource(private val apiService: ApiService) {
    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(apiService: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(apiService)
            }
    }
    fun getGameDetail(id: Int): Flow<ApiResponse<GamesDetailResponse>> = flow {
        try {
            val response = apiService.getGameDetail(id)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message ?: "Unknown error occurred"))
        }
    }
    fun getAllGames(): Flow<ApiResponse<List<ResultsItem?>?>> = flow {
        val response = apiService.getGames()
        val results = response.results

        if (results!!.isNotEmpty()) {
            emit(ApiResponse.Success(results))
        } else {
            emit(ApiResponse.Empty)
        }
    }.catch { e ->
        emit(ApiResponse.Error(e.message ?: "Unknown error"))
    }
}