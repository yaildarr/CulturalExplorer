package ru.ildar.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.ildar.network.dto.BookSearchResponse

interface BookApiService {
    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): BookSearchResponse
}