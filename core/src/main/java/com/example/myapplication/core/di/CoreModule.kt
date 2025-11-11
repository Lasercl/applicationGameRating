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
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val databaseModule = module {
    factory { get<GamesDatabase>().GamesDao() }
    single {
        val context = androidContext()
        context.deleteDatabase("Games.db") // <--- hapus file lama (non-encrypted)

        val passphrase: ByteArray = SQLiteDatabase.getBytes("dicoding".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            GamesDatabase::class.java, "Games.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single {
        val hostname = "api.rawg.io"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/C6lwlbvuGDHvMA0ZDlyui0NI6TX2XKXitrKamh2jayM=")
            .add(hostname, "sha256/kIdp6NNEd8wsugYyyIYFsi1ylMCED3hZbSR8ZFsa/A4=")
            .add(hostname, "sha256/mEflZT5enoR1FuXLgYYGqnVEoZvmf9c2bVBpiOjYQ0c=")
            .build()
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
            .certificatePinner(certificatePinner)
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