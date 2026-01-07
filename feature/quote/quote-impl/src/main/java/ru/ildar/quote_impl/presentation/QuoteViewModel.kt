package ru.ildar.quote_impl.presentation

import android.util.Log
import org.orbitmvi.orbit.ContainerHost
import ru.ildar.quote_api.usecase.GetRandomQuoteUseCase
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import org.orbitmvi.orbit.viewmodel.container
import ru.ildar.domain.model.MyResult
import com.google.firebase.analytics.logEvent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class QuoteViewModel(
    private val getRandomQuoteUseCase: GetRandomQuoteUseCase
) : ContainerHost<QuoteState, Nothing>, ViewModel() {

    override val container = container<QuoteState, Nothing>(QuoteState())

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "QuoteScreen")
        }
    }

    fun onAction(action: QuoteAction) = intent {
        when (action) {
            QuoteAction.LoadRandom -> {
                reduce { state.copy(isLoading = true) }
                val result = getRandomQuoteUseCase.invoke()
                when(result){
                    is MyResult.Error -> {
                        Log.d("MyLog","error ${result.message}")
                        reduce {
                            state.copy(
                                isLoading = false,
                                messageError = result.message
                            )
                        }
                    }
                    is MyResult.Success -> {
                        reduce {
                            state.copy(
                                isLoading = false,
                                quote = result.data
                            )
                        }
                    }
                }
            }
        }
    }

}
