package ru.ildar.book_api.model.usecase

import ru.ildar.book_api.model.repository.BookRepository
import ru.ildar.domain.model.Book
import ru.ildar.domain.model.MyResult

class SearchBooksUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(query: String): MyResult<List<Book>>{
        val result = bookRepository.searchBooks(query)
        return when (result){
            is MyResult.Error -> {
                MyResult.Error(result.message)
            }
            is MyResult.Success -> {
                val list = result.data.map { model ->
                    Book(
                        id = model.id,
                        title = model.title,
                        authors = model.authors,
                        firstPublishYear = model.firstPublishYear,
                        coverId = model.coverId,
                        language = model.language
                    )
                }
                MyResult.Success(list)
            }
        }
    }
}