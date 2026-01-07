package ru.ildar.quote_api.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.ildar.domain.model.MyResult
import ru.ildar.domain.model.Quote
import ru.ildar.quote_api.repository.QuoteRepository

@ExperimentalCoroutinesApi
class GetRandomQuoteUseCaseTest {

    private lateinit var getRandomQuoteUseCase: GetRandomQuoteUseCase
    private lateinit var mockRepository: QuoteRepository

    @Before
    fun setup() {
        mockRepository = mockk()
        getRandomQuoteUseCase = GetRandomQuoteUseCase(mockRepository)
    }

    @Test
    fun `invoke should return Success with quote when repository returns Success`() = runTest {
        // Arrange
        val mockQuote = Quote(
            id = "1poKfDkgQl6w",
            content = "People may doubt what you say, but they will believe what you do.",
            author = "Lewis Cass",
        )

        coEvery { mockRepository.getRandomQuote() } returns MyResult.Success(mockQuote)

        // Act
        val result = getRandomQuoteUseCase.invoke()

        // Assert
        assertTrue(result is MyResult.Success)
        val successResult = result as MyResult.Success
        assertEquals(mockQuote.id, successResult.data.id)
        assertEquals(mockQuote.content, successResult.data.content)
        assertEquals(mockQuote.author, successResult.data.author)
    }

    @Test
    fun `invoke should return Error when repository returns Error`() = runTest {
        // Arrange
        val errorMessage = "Failed to fetch quote"
        coEvery { mockRepository.getRandomQuote() } returns MyResult.Error(errorMessage)

        // Act
        val result = getRandomQuoteUseCase.invoke()

        // Assert
        assertTrue(result is MyResult.Error)
        val errorResult = result as MyResult.Error
        assertEquals(errorMessage, errorResult.message)
    }

    @Test
    fun `invoke should handle null values in quote`() = runTest {
        // Arrange
        val mockQuote = Quote(
            id = "test123",
            content = "Test content",
            author = "Test Author",
        )

        coEvery { mockRepository.getRandomQuote() } returns MyResult.Success(mockQuote)

        // Act
        val result = getRandomQuoteUseCase.invoke()

        // Assert
        assertTrue(result is MyResult.Success)
        val successResult = result as MyResult.Success
    }

    @Test
    fun `invoke should not modify the quote data`() = runTest {
        // Arrange
        val originalQuote = Quote(
            id = "originalId",
            content = "Original content",
            author = "Original Author",

        )

        coEvery { mockRepository.getRandomQuote() } returns MyResult.Success(originalQuote)

        // Act
        val result = getRandomQuoteUseCase.invoke()

        // Assert
        val returnedQuote = (result as MyResult.Success).data
        // Проверяем, что все поля идентичны
        assertEquals(originalQuote.id, returnedQuote.id)
        assertEquals(originalQuote.content, returnedQuote.content)
        assertEquals(originalQuote.author, returnedQuote.author)
        assertEquals(originalQuote.tags, returnedQuote.tags)
        assertEquals(originalQuote.authorSlug, returnedQuote.authorSlug)
        assertEquals(originalQuote.length, returnedQuote.length)
        assertEquals(originalQuote.dateAdded, returnedQuote.dateAdded)
        assertEquals(originalQuote.dateModified, returnedQuote.dateModified)
    }

    @Test
    fun `invoke should call repository exactly once`() = runTest {
        // Arrange
        var callCount = 0
        val mockQuote = Quote(
            id = "test",
            content = "test",
            author = "test",
            tags = emptyList(),
            authorSlug = "",
            length = 0,
            dateAdded = null,
            dateModified = null
        )

        coEvery { mockRepository.getRandomQuote() } coAnswers {
            callCount++
            MyResult.Success(mockQuote)
        }

        // Act
        getRandomQuoteUseCase.invoke()

        // Assert
        assertEquals(1, callCount)
    }
}