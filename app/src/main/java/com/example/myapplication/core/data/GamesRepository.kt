package com.example.myapplication.core.data

import DataMapper
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.room.util.query
import com.example.myapplication.core.data.domain.model.Game
import com.example.myapplication.core.data.source.local.LocalDataSource
import com.example.myapplication.core.data.source.remote.RemoteDataSource
import com.example.myapplication.core.data.source.remote.network.ApiResponse
import com.example.myapplication.core.data.source.remote.response.ResultsItem
import com.example.myapplication.core.utils.AppExecutors
import com.example.myapplication.core.domain.repository.IGamesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GamesRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IGamesRepository {

    companion object {
        @Volatile
        private var instance: GamesRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): GamesRepository =
            instance ?: synchronized(this) {
                instance ?: GamesRepository(remoteData, localData, appExecutors)
            }
    }

    // 🔹 Ambil semua game
    override fun getAllGames(): Flow<Resource<List<Game>>> =
        object : NetworkBoundResource<List<Game>, List<ResultsItem>>() {

            override fun loadFromDB(): Flow<List<Game>> {
                return localDataSource.getAllGames().map {
                    DataMapper.mapEntitiesToDomain(it)
                } as Flow<List<Game>>
            }

            override fun shouldFetch(data: List<Game>?): Boolean =
                data.isNullOrEmpty() // fetch kalau DB kosong

            override suspend fun createCall(): Flow<ApiResponse<List<ResultsItem>>> =
                remoteDataSource.getAllGames() as Flow<ApiResponse<List<ResultsItem>>>

            override suspend fun saveCallResult(data: List<ResultsItem>) {
                val gameEntities = DataMapper.mapResponsesToEntities(data)
                withContext(Dispatchers.IO) {
                    localDataSource.insertGames(gameEntities)
                }
            }
        }.asFlow()


    override fun getGameDetail(id: Int): Flow<Resource<Game>> = flow {
        emit(Resource.Loading())

        remoteDataSource.getGameDetail(id).collect { apiResponse ->
            when (apiResponse) {
                is ApiResponse.Success -> {
                    val remoteGameEntity = DataMapper.mapDetailToEntity(apiResponse.data)

                    val localGame = localDataSource.getGameById(id).firstOrNull()
                    val isFavorite = localGame?.isFavorite ?: false

                    val finalGame = DataMapper.mapEntityToDomain(remoteGameEntity)
                        .copy(isFavorite = isFavorite)

                    emit(Resource.Success(finalGame))
                }

                is ApiResponse.Error -> emit(Resource.Error(apiResponse.errorMessage))
                is ApiResponse.Empty -> emit(Resource.Error("No data found"))
            }
        }
    }


    // 🔹 Ambil favorit
    override fun getFavoriteGames(): Flow<List<Game>> {
        return localDataSource.getFavoriteGames().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    // 🔹 Set favorit
    override fun setFavoriteGame(game: Game, state: Boolean) {
        val gameEntity = DataMapper.mapDomainToEntity(game)
        appExecutors.diskIO().execute {
            localDataSource.setFavoriteGames(gameEntity, state)
        }
    }
}