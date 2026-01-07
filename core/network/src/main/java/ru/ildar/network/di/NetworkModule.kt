package ru.ildar.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ildar.network.BookApiService
import ru.ildar.network.adapter.DescriptionTypeAdapter
import ru.ildar.network.dto.DescriptionDto
import ru.ildar.util.Constants


val MOVIE_API = named("MOVIE_API")
val BOOK_API= named("BOOK_API")

val networkModule = module {
    single<OkHttpClient> {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
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
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
    single {
        get<Retrofit>(BOOK_API).create(BookApiService::class.java)
    }

    single<Gson>{
        GsonBuilder()
            .registerTypeAdapter(
                DescriptionDto::class.java,
                DescriptionTypeAdapter()
            )
            .create()
    }


//    single {
//        get<Retrofit>(API_2).create(SecondApi::class.java)
//    }
}