package ru.ildar.book_impl.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.ildar.book_api.model.repository.BookRepository
import ru.ildar.book_api.model.usecase.SearchBooksUseCase
import ru.ildar.book_impl.booklist.presentation.BookListViewModel
import ru.ildar.book_impl.repository.BookRepositoryImpl

val bookModule = module {


    single<BookRepository> {
        BookRepositoryImpl(get())
    }

    // Use Cases
    single { SearchBooksUseCase(bookRepository = get()) }


    viewModel {
        BookListViewModel(searchBooksUseCase = get())
    }

}