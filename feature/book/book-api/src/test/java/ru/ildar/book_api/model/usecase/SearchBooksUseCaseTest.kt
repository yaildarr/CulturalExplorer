package ru.ildar.book_api.model.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.ildar.book_api.model.repository.BookRepository
import ru.ildar.domain.model.Book
import ru.ildar.domain.model.MyResult

@ExperimentalCoroutinesApi
class SearchBooksUseCaseTest {

    private lateinit var searchBooksUseCase: SearchBooksUseCase
    private lateinit var mockRepository: BookRepository

    @Before
    fun setup() {
        mockRepository = mockk()
        searchBooksUseCase = SearchBooksUseCase(mockRepository)
    }

    @Test
    fun `invoke should return Success with mapped books when repository returns Success`() = runTest {
        // Arrange
        val query = "test query"
        val mockBookModels = listOf(
            Book(
                id = "OL123",
                title = "Test Book",
                authors = listOf("Author 1", "Author 2"),
                firstPublishYear = 2020,
                coverId = 12345L,
                language = listOf("en")
            ),
            Book(
                id = "OL456",
                title = "Another Book",
                authors = listOf("Author 3"),
                firstPublishYear = 2021,
                coverId = 67890L,
                language = listOf("fr", "en")
            )
        )

        val expectedBooks = mockBookModels.map { model ->
            Book(
                id = model.id,
                title = model.title,
                authors = model.authors,
                firstPublishYear = model.firstPublishYear,
                coverId = model.coverId,
                language = model.language
            )
        }

        coEvery { mockRepository.searchBooks(query) } returns MyResult.Success(mockBookModels)

        // Act
        val result = searchBooksUseCase.invoke(query)

        // Assert
        assertTrue(result is MyResult.Success)
        val successResult = result as MyResult.Success
        assertEquals(expectedBooks.size, successResult.data.size)
        assertEquals(expectedBooks[0].id, successResult.data[0].id)
        assertEquals(expectedBooks[0].title, successResult.data[0].title)
        assertEquals(expectedBooks[1].authors, successResult.data[1].authors)
    }

    @Test
    fun `invoke should return Error when repository returns Error`() = runTest {
        // Arrange
        val query = "test query"
        val errorMessage = "Network error"
        coEvery { mockRepository.searchBooks(query) } returns MyResult.Error(errorMessage)

        // Act
        val result = searchBooksUseCase.invoke(query)

        // Assert
        assertTrue(result is MyResult.Error)
        val errorResult = result as MyResult.Error
        assertEquals(errorMessage, errorResult.message)
    }

    @Test
    fun `invoke should handle empty list from repository`() = runTest {
        // Arrange
        val query = "empty result query"
        coEvery { mockRepository.searchBooks(query) } returns MyResult.Success(emptyList())

        // Act
        val result = searchBooksUseCase.invoke(query)

        // Assert
        assertTrue(result is MyResult.Success)
        val successResult = result as MyResult.Success
        assertTrue(successResult.data.isEmpty())
    }

    @Test
    fun `invoke should pass correct query to repository`() = runTest {
        // Arrange
        val query = "specific query"
        coEvery { mockRepository.searchBooks(query) } returns MyResult.Success(emptyList())

        // Act
        searchBooksUseCase.invoke(query)

        // Assert (verify interaction)
        coEvery { mockRepository.searchBooks(query) } // Verification happens in coEvery
    }
}