package ru.ildar.book_impl.repository

import ru.ildar.book_api.model.repository.BookRepository
import ru.ildar.domain.model.Book
import ru.ildar.domain.model.MyResult
import ru.ildar.network.BookApiService

class BookRepositoryImpl(
    private val api: BookApiService
) : BookRepository {
    override suspend fun searchBooks(query: String): MyResult<List<Book>> {
        return try {
            val response = api.searchBooks(query, 30)
            val books = response.docs.map { dto ->
                Book(
                    id = dto.key,
                    title = dto.title,
                    authors = dto.authorName ?: emptyList(),
                    firstPublishYear = dto.firstPublishYear,
                    coverId = dto.coverId,
                    language = dto.language
                )
            }
            MyResult.Success(books)
        } catch (e: Exception) {
            MyResult.Error(e.message.toString())
        }
    }
}