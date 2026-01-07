package ru.ildar.culturalexplorer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.ildar.auth_impl.di.authModule
import ru.ildar.book_impl.di.bookModule
import ru.ildar.culturalexplorer.di.appModule
import ru.ildar.network.di.networkModule
import ru.ildar.quote_impl.di.quoteModule

class CulturalApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CulturalApp)
            modules(listOf(authModule, appModule, bookModule, networkModule, quoteModule))
        }
    }
}
