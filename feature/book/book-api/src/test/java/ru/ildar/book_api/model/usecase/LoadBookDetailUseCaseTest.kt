// GetRandomQuoteUseCaseTest.kt
package ru.ildar.quote_api.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.ildar.book_api.model.repository.BookRepository
import ru.ildar.book_api.model.usecase.LoadBookDetailUseCase
import ru.ildar.domain.model.BookDetails
import ru.ildar.domain.model.MyResult
@ExperimentalCoroutinesApi
class LoadBookDetailUseCaseTest {

    private lateinit var loadBookDetailUseCase: LoadBookDetailUseCase
    private lateinit var mockRepository: BookRepository

    @Before
    fun setup() {
        mockRepository = mockk()
        loadBookDetailUseCase = LoadBookDetailUseCase(mockRepository)
    }

    @Test
    fun `invoke should return Success with book details when repository returns Success`() = runTest {
        // Arrange
        val bookId = "OL123"
        val mockBookDetails = BookDetails(
            id = bookId,
            title = "Test Book Details",
            description = "A test description",
            coverId = 12345L,
            firstPublishYear = "2020",
            subjects = listOf("Fiction", "Science")
        )

        coEvery { mockRepository.loadDetailBook(bookId) } returns MyResult.Success(mockBookDetails)

        // Act
        val result = loadBookDetailUseCase.invoke(bookId)

        // Assert
        assertTrue(result is MyResult.Success)
        val successResult = result as MyResult.Success
        assertEquals(mockBookDetails.id, successResult.data.id)
        assertEquals(mockBookDetails.title, successResult.data.title)
        assertEquals(mockBookDetails.description, successResult.data.description)
        assertEquals(mockBookDetails.coverId, successResult.data.coverId)
        assertEquals(mockBookDetails.subjects, successResult.data.subjects)
    }

    @Test
    fun `invoke should return Error when repository returns Error`() = runTest {
        // Arrange
        val bookId = "OL123"
        val errorMessage = "Book not found"
        coEvery { mockRepository.loadDetailBook(bookId) } returns MyResult.Error(errorMessage)

        // Act
        val result = loadBookDetailUseCase.invoke(bookId)

        // Assert
        assertTrue(result is MyResult.Error)
        val errorResult = result as MyResult.Error
        assertEquals(errorMessage, errorResult.message)
    }

    @Test
    fun `invoke should propagate repository error message`() = runTest {
        // Arrange
        val bookId = "OL999"
        val expectedErrorMessage = "Failed to load book details: Network timeout"
        coEvery { mockRepository.loadDetailBook(bookId) } returns MyResult.Error(expectedErrorMessage)

        // Act
        val result = loadBookDetailUseCase.invoke(bookId)

        // Assert
        assertEquals(expectedErrorMessage, (result as MyResult.Error).message)
    }

    @Test
    fun `invoke should pass correct book ID to repository`() = runTest {
        // Arrange
        val bookId = "OL12345"
        coEvery { mockRepository.loadDetailBook(bookId) } returns MyResult.Success(
            BookDetails(
                id = bookId, title = "Test",
                description = "",
                coverId = 0,
                firstPublishYear = "",
                subjects = emptyList()
            )
        )

        // Act
        val result = loadBookDetailUseCase.invoke(bookId)

        // Assert
        assertTrue(result is MyResult.Success)
        assertEquals(bookId, (result as MyResult.Success).data.id)
    }
}
