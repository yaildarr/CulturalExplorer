package ru.ildar.book_impl.bookdetail.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.ildar.book_api.model.usecase.LoadBookDetailUseCase
import ru.ildar.domain.model.BookDetails
import ru.ildar.domain.model.MyResult


class BookDetailViewModel(
    private val useCase: LoadBookDetailUseCase
)  : ContainerHost<BookDetailState, BookDetailSideEffect>, ViewModel(){

    override val container = container<BookDetailState, BookDetailSideEffect>(BookDetailState())

    fun onAction(action: BookDetailAction) = intent {
        when(action){
            is BookDetailAction.DetailsOpen -> {
                Log.d("MyLog","Id = ${action.id}")
                reduce { state.copy(isLoading = true) }
                val result = useCase.invoke(action.id)
                when(result){
                    is MyResult.Error -> {
                        Log.d("MyLog","ошибка, ${result.message}")
                    }
                    is MyResult.Success<BookDetails> -> {
                        Log.d("MyLog","бук ${result.data.toString()}")
                        reduce {
                            state.copy(
                                book = result.data
                            )
                        }
                    }
                }
            }
        }
    }

}