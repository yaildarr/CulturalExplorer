package ru.ildar.quote_impl.presentation

import ru.ildar.domain.model.Quote

data class QuoteState(
    val quote: Quote? = null,
    val isLoading: Boolean = false,
    val messageError: String = ""
)

sealed interface QuoteAction {
    object LoadRandom : QuoteAction
}
