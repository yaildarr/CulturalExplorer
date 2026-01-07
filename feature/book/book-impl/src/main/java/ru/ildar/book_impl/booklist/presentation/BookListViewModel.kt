package ru.ildar.book_impl.booklist.presentation

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import org.orbitmvi.orbit.ContainerHost
import ru.ildar.book_api.model.usecase.SearchBooksUseCase
import org.orbitmvi.orbit.viewmodel.container
import ru.ildar.domain.model.Book
import ru.ildar.domain.model.MyResult
import com.google.firebase.analytics.logEvent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics


class BookListViewModel(
    private val searchBooksUseCase: SearchBooksUseCase
) : ContainerHost<BookState,BookSideEffect>, ViewModel(){

    override val container = container<BookState, BookSideEffect>(BookState())


    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "BookListScreen")
        }
    }


    fun onAction(action: BookAction) = intent {
        when (action) {
            is BookAction.QueryChanged -> {
                reduce { state.copy(query = action.query, hasSearched = false) }
            }
            BookAction.SearchClicked -> {
                if (state.query.isBlank()) return@intent
                reduce { state.copy(isLoading = true, isError = false, errorMessage = null, hasSearched = true) }
                val result = searchBooksUseCase.invoke(state.query)
                when (result) {
                    is MyResult.Success<List<Book>> -> {
                        val newBooks = result.data.map { book ->
                            Book(
                                id = book.id,
                                title = book.title,
                                authors = book.authors,
                                firstPublishYear = book.firstPublishYear,
                                coverId = book.coverId,
                                language = book.language
                            )
                        }
                        reduce {
                            state.copy(
                                isLoading = false,
                                books = newBooks,
                            )
                        }
                    }
                    is MyResult.Error -> {
                        val msg = result.message ?: "Не удалось найти книги"
                        reduce {
                            state.copy(
                                isLoading = false,
                                isError = true,
                                errorMessage = msg,
                                books = emptyList()
                            )
                        }
                        postSideEffect(BookSideEffect.ShowError(msg))
                    }
                }
            }
        }
    }

}