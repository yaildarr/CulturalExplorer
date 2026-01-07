package ru.ildar.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ildar.domain.model.Quote
import ru.ildar.network.BookApiService
import ru.ildar.network.QuoteApiService
import ru.ildar.network.adapter.DescriptionTypeAdapter
import ru.ildar.network.dto.DescriptionDto
import ru.ildar.util.Constants
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


val QUOTE_API = named("QUOTE_API")
val BOOK_API= named("BOOK_API")

val networkModule = module {
    single<OkHttpClient> {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        //у второй апишки нет ssl сертификата, поэтому так :(
        OkHttpClient.Builder().apply {
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                })

                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())

                sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
                hostnameVerifier { _, _ -> true }
        }.addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    single(QUOTE_API) {
        Retrofit.Builder()
            .baseUrl(Constants.QUOTE_URL)
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


    single {
        get<Retrofit>(QUOTE_API).create(QuoteApiService::class.java)
    }
}