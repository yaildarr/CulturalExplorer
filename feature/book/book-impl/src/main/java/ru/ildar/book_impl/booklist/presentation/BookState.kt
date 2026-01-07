package ru.ildar.book_impl.booklist.presentation

import ru.ildar.domain.model.Book

data class BookState(
    val query: String = "",
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val hasSearched: Boolean = false
)

sealed interface BookAction {
    data class QueryChanged(val query: String) : BookAction
    object SearchClicked : BookAction
}

sealed interface BookSideEffect {
    data class ShowError(val message: String) : BookSideEffect
}