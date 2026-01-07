package ru.ildar.book_impl.bookdetail.presentation

import ru.ildar.domain.model.BookDetails


data class BookDetailState(
    val book: BookDetails = BookDetails(
        id = "",
        title = "",
        description = "",
        coverId = null,
        firstPublishYear = "",
        subjects = emptyList()
    ),
    val isLoading: Boolean = false
)

sealed interface BookDetailAction {
    data class DetailsOpen(val id: String) : BookDetailAction
}

sealed interface BookDetailSideEffect {
    data class ShowError(val message: String) : BookDetailSideEffect
}