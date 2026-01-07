package ru.ildar.quote_api.repository

import ru.ildar.domain.model.MyResult
import ru.ildar.domain.model.Quote

interface QuoteRepository {
    suspend fun getRandomQuote() : MyResult<Quote>
}