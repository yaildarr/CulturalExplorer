package ru.ildar.culturalexplorer.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.ildar.culturalexplorer.AppViewModel

val appModule = module {
    single { Dispatchers.IO }

    viewModel {
        AppViewModel(authRepository = get())
    }
}