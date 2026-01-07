package ru.ildar.quote_api.usecase

import ru.ildar.domain.model.MyResult
import ru.ildar.domain.model.Quote
import ru.ildar.quote_api.repository.QuoteRepository

class GetRandomQuoteUseCase(
    private val quoteRepository: QuoteRepository
) {
    suspend operator fun invoke(): MyResult<Quote>{
        val result = quoteRepository.getRandomQuote()
        return when (result){
            is MyResult.Error -> {
                MyResult.Error(result.message)
            }
            is MyResult.Success -> {
                MyResult.Success(result.data)
            }
        }
    }
}