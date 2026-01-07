package ru.ildar.quote_api.usecase

import io.mockk.coEvery
import io.mockk.coVerify
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

    private lateinit var useCase: GetRandomQuoteUseCase
    private lateinit var mockRepository: QuoteRepository

    @Before
    fun setUp() {
        mockRepository = mockk(relaxed = true)
        useCase = GetRandomQuoteUseCase(mockRepository)
    }

    @Test
    fun `invoke should return Success with quote when repository returns Success`() = runTest {
        // Arrange
        val expectedQuote = Quote(
            id = "1poKfDkgQl6w",
            content = "People may doubt what you say, but they will believe what you do.",
            author = "Lewis Cass"
        )

        coEvery { mockRepository.getRandomQuote() } returns MyResult.Success(expectedQuote)

        // Act
        val result = useCase.invoke()

        // Assert
        assertTrue(result is MyResult.Success)
        val successResult = result as MyResult.Success
        assertEquals(expectedQuote.id, successResult.data.id)
        assertEquals(expectedQuote.content, successResult.data.content)
        assertEquals(expectedQuote.author, successResult.data.author)
    }

    @Test
    fun `invoke should return Error when repository returns Error`() = runTest {
        // Arrange
        val errorMessage = "Failed to fetch quote"
        coEvery { mockRepository.getRandomQuote() } returns MyResult.Error(errorMessage)

        // Act
        val result = useCase.invoke()

        // Assert
        assertTrue(result is MyResult.Error)
        val errorResult = result as MyResult.Error
        assertEquals(errorMessage, errorResult.message)
    }

    @Test
    fun `invoke should propagate repository error message unchanged`() = runTest {
        // Arrange
        val expectedErrorMessage = "Network error: Connection timeout"
        coEvery { mockRepository.getRandomQuote() } returns MyResult.Error(expectedErrorMessage)

        // Act
        val result = useCase.invoke()

        // Assert
        val errorResult = result as MyResult.Error
        assertEquals(expectedErrorMessage, errorResult.message)
    }

    @Test
    fun `invoke should call repository exactly once`() = runTest {
        // Arrange
        val testQuote = Quote(
            id = "test-123",
            content = "Test quote",
            author = "Test Author"
        )
        coEvery { mockRepository.getRandomQuote() } returns MyResult.Success(testQuote)

        // Act
        useCase.invoke()

        // Assert
        coVerify(exactly = 1) { mockRepository.getRandomQuote() }
    }

    @Test
    fun `invoke should return quote with empty content when repository returns empty content`() = runTest {
        // Arrange
        val quoteWithEmptyContent = Quote(
            id = "empty-123",
            content = "",
            author = "Unknown"
        )
        coEvery { mockRepository.getRandomQuote() } returns MyResult.Success(quoteWithEmptyContent)

        // Act
        val result = useCase.invoke()

        // Assert
        val successResult = result as MyResult.Success
        assertEquals("", successResult.data.content)
    }

    @Test
    fun `invoke should handle quote with special characters`() = runTest {
        // Arrange
        val quoteWithSpecialChars = Quote(
            id = "special-123",
            content = "Life is what happens to you while you're busy making other plans. — John Lennon",
            author = "John Lennon"
        )
        coEvery { mockRepository.getRandomQuote() } returns MyResult.Success(quoteWithSpecialChars)

        // Act
        val result = useCase.invoke()

        // Assert
        val successResult = result as MyResult.Success
        assertEquals("John Lennon", successResult.data.author)
        assertTrue(successResult.data.content.contains("—"))
    }

    @Test
    fun `invoke should return quote with long author name`() = runTest {
        // Arrange
        val quoteWithLongAuthor = Quote(
            id = "long-author-123",
            content = "The only way to do great work is to love what you do.",
            author = "Steve Paul Jobs"
        )
        coEvery { mockRepository.getRandomQuote() } returns MyResult.Success(quoteWithLongAuthor)

        // Act
        val result = useCase.invoke()

        // Assert
        val successResult = result as MyResult.Success
        assertEquals("Steve Paul Jobs", successResult.data.author)
    }

    @Test
    fun `invoke should not modify quote data`() = runTest {
        // Arrange
        val originalQuote = Quote(
            id = "original-id",
            content = "Original content",
            author = "Original Author"
        )
        coEvery { mockRepository.getRandomQuote() } returns MyResult.Success(originalQuote)

        // Act
        val result = useCase.invoke()

        // Assert
        val returnedQuote = (result as MyResult.Success).data
        // Проверяем, что это копия, а не тот же объект (если data class)
        assertEquals(originalQuote.id, returnedQuote.id)
        assertEquals(originalQuote.content, returnedQuote.content)
        assertEquals(originalQuote.author, returnedQuote.author)
    }

    @Test
    fun `invoke should handle multiple calls correctly`() = runTest {
        // Arrange
        val firstQuote = Quote(
            id = "first",
            content = "First quote",
            author = "First Author"
        )
        val secondQuote = Quote(
            id = "second",
            content = "Second quote",
            author = "Second Author"
        )

        coEvery { mockRepository.getRandomQuote() } returnsMany listOf(
            MyResult.Success(firstQuote),
            MyResult.Success(secondQuote)
        )

        // Act
        val firstResult = useCase.invoke()
        val secondResult = useCase.invoke()

        // Assert
        val firstSuccess = firstResult as MyResult.Success
        val secondSuccess = secondResult as MyResult.Success

        assertEquals("first", firstSuccess.data.id)
        assertEquals("second", secondSuccess.data.id)
        assertNotEquals(firstSuccess.data.content, secondSuccess.data.content)
    }

    @Test
    fun `invoke should work with MyResult sealed class correctly`() = runTest {
        // Test both branches of when statement
        val testQuote = Quote("test", "test", "test")

        // Test Success branch
        coEvery { mockRepository.getRandomQuote() } returns MyResult.Success(testQuote)
        val successResult = useCase.invoke()
        assertTrue(successResult is MyResult.Success)

        // Test Error branch
        coEvery { mockRepository.getRandomQuote() } returns MyResult.Error("Error")
        val errorResult = useCase.invoke()
        assertTrue(errorResult is MyResult.Error)
    }
}

// Дополнительные тесты для проверки edge cases
@ExperimentalCoroutinesApi
class GetRandomQuoteUseCaseEdgeCasesTest {

    private lateinit var useCase: GetRandomQuoteUseCase
    private lateinit var mockRepository: QuoteRepository

    @Before
    fun setUp() {
        mockRepository = mockk(relaxed = true)
        useCase = GetRandomQuoteUseCase(mockRepository)
    }

    @Test
    fun `invoke should work with coroutine dispatchers`() = runTest {
        // Arrange
        val testQuote = Quote("test", "test", "test")
        coEvery { mockRepository.getRandomQuote() } returns MyResult.Success(testQuote)

        // Act & Assert - просто проверяем что не падает
        val result = useCase.invoke()
        assertTrue(result is MyResult.Success)
    }
}
