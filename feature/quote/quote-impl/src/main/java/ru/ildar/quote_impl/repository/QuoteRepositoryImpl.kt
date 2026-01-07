package ru.ildar.quote_impl.repository

import ru.ildar.domain.model.MyResult
import ru.ildar.domain.model.Quote
import ru.ildar.network.QuoteApiService
import ru.ildar.quote_api.repository.QuoteRepository
import ru.ildar.quote_api.usecase.GetRandomQuoteUseCase

class QuoteRepositoryImpl(
    private val apiService: QuoteApiService
) : QuoteRepository {
    override suspend fun getRandomQuote(): MyResult<Quote> {
        return try {
            val response = apiService.getRandomQuote()
            val resp = response.first()
            MyResult.Success(
                Quote(
                    id = resp.id,
                    content = resp.content,
                    author = resp.author
                )
            )
        } catch (e: Exception){
            MyResult.Error(e.message.toString())
        }
    }
}