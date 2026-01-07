package ru.ildar.network.di

import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ildar.network.BookApiService
import ru.ildar.util.Constants


val MOVIE_API = named("MOVIE_API")
val BOOK_API= named("BOOK_API")

val networkModule = module {
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .build()
    }
    single(MOVIE_API) {
        Retrofit.Builder()
            .baseUrl(Constants.MOVIE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single(BOOK_API) {
        Retrofit.Builder()
            .baseUrl(Constants.BOOK_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single {
        get<Retrofit>(BOOK_API).create(BookApiService::class.java)
    }

//    single {
//        get<Retrofit>(API_2).create(SecondApi::class.java)
//    }
}