package ru.ildar.quote_impl.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

import ru.ildar.quote_api.repository.QuoteRepository
import ru.ildar.quote_api.usecase.GetRandomQuoteUseCase
import ru.ildar.quote_impl.presentation.QuoteViewModel
import ru.ildar.quote_impl.repository.QuoteRepositoryImpl

val quoteModule = module {


    single<QuoteRepository> {
        QuoteRepositoryImpl(apiService = get())
    }

    // Use Cases
    single<GetRandomQuoteUseCase>
    { GetRandomQuoteUseCase(quoteRepository = get()) }


    viewModel {
        QuoteViewModel(getRandomQuoteUseCase = get())
    }
}