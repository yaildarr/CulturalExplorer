package ru.ildar.culturalexplorer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.ildar.auth_impl.di.authModule
import ru.ildar.culturalexplorer.di.appModule

class CulturalApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CulturalApp)
            modules(listOf(authModule, appModule))
        }
    }
}