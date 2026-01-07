package ru.ildar.network

import retrofit2.http.GET
import ru.ildar.network.dto.QuoteResponse

interface QuoteApiService {
    @GET("random")
    suspend fun getRandomQuote(): List<QuoteResponse>
}