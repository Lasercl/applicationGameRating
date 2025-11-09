package com.example.myapplication.core.di

import androidx.room.Room
import com.example.myapplication.core.BuildConfig
import com.example.myapplication.core.data.GamesRepository
import com.example.myapplication.core.data.source.local.LocalDataSource
import com.example.myapplication.core.data.source.local.room.GamesDatabase
import com.example.myapplication.core.data.source.remote.RemoteDataSource
import com.example.myapplication.core.repository.IGamesRepository
import com.example.myapplication.core.utils.AppExecutors
import com.example.myapplication.retrofit.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val databaseModule = module {
    factory { get<GamesDatabase>().GamesDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            GamesDatabase::class.java, "Games.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val originalUrl = original.url

                val newUrl = originalUrl.newBuilder()
                    .addQueryParameter("key", BuildConfig.RAWG_API_KEY)
                    .build()

                val newRequest = original.newBuilder().url(newUrl).build()
                chain.proceed(newRequest)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.rawg.io/api/") // jangan lupa pakai URL lengkap
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IGamesRepository> { GamesRepository(get(), get(), get()) }
}