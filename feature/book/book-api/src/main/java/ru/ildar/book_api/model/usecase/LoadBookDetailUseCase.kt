package ru.ildar.book_api.model.usecase

import ru.ildar.book_api.model.repository.BookRepository
import ru.ildar.domain.model.Book
import ru.ildar.domain.model.BookDetails
import ru.ildar.domain.model.MyResult

class LoadBookDetailUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(query: String): MyResult<BookDetails>{
        val result = bookRepository.loadDetailBook(query)
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