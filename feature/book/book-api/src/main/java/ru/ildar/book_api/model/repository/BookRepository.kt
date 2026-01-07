package ru.ildar.book_api.model.repository

import ru.ildar.domain.model.Book
import ru.ildar.domain.model.BookDetails
import ru.ildar.domain.model.MyResult

interface BookRepository {
    suspend fun searchBooks(query: String) : MyResult<List<Book>>

    suspend fun loadDetailBook(bookId: String) : MyResult<BookDetails>
}