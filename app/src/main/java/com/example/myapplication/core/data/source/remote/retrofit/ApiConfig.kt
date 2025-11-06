package com.example.myapplication.retrofit

import com.example.myapplication.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

    class ApiConfig {
        companion object {
            fun getApiService(): ApiService {
                val loggingInterceptor =
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

                val client = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // <--- tambahkan ini
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

                return retrofit.create(ApiService::class.java)
            }
        }
    }
