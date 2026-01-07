package ru.ildar.auth_impl.ui.signin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.ildar.api.usecase.SignInUseCase
import ru.ildar.domain.model.AuthResult
import com.google.firebase.analytics.logEvent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
) : ContainerHost<SignInState, SignInSideEffect>, ViewModel() {


    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "SignInScreen")
        }
    }

    override val container = container<SignInState, SignInSideEffect>(SignInState())

    fun onAction(action: SignInAction) = intent {
        when (action) {
            is SignInAction.EmailChanged -> {
                val newState = state.copy(email = action.email)
                reduce { validateForm(newState) }
            }

            is SignInAction.PasswordChanged -> {
                val newState = state.copy(password = action.password)
                reduce {validateForm(newState)}
            }

            SignInAction.SignInClicked -> {
                val newState = state.copy(isLoading = true, errorMessage = null)
                reduce {
                    newState
                }
                val result = signInUseCase.invoke(state.email,state.password)
                when (result){
                    is AuthResult.Error -> {
                        reduce {
                            newState.copy(isLoading = false, errorMessage = result.message)
                        }
                    }
                    is AuthResult.Success -> {
                        reduce {
                            newState.copy(isLoading = false, isSuccess = true)
                        }
                        postSideEffect(SignInSideEffect.ShowSuccess)
                    }
                }
            }
        }
    }

    private fun validateForm(state: SignInState): SignInState {
        val isEmailValid = state.email.isNotBlank() &&
                Patterns.EMAIL_ADDRESS.matcher(state.email).matches()
        val isPasswordValid = state.password.length >= 6
        return state.copy(isSignInEnabled = isEmailValid && isPasswordValid)
    }


}