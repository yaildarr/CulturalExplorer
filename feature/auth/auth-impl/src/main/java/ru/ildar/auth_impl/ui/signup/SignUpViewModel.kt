package ru.ildar.auth_impl.ui.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.ildar.api.usecase.SignUpUseCase
import ru.ildar.domain.model.AuthResult


class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
) : ContainerHost<SignUpState, SignUpSideEffect>, ViewModel() {

    override val container = container<SignUpState, SignUpSideEffect>(SignUpState())

    fun onAction(action: SignUpAction) = intent {
        when (action) {
            is SignUpAction.EmailChanged -> {
                val newState = state.copy(email = action.email)
                reduce { validateForm(newState) }
            }

            is SignUpAction.PasswordChanged -> {
                val newState = state.copy(password = action.password)
                reduce {validateForm(newState)}
            }

            SignUpAction.SignUpClicked -> {
                val newState = state.copy(isLoading = true, errorMessage = null)
                reduce {
                    newState
                }
                val result = signUpUseCase.invoke(state.email,state.password)
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
                    }
                }
            }
        }
    }

    private fun validateForm(state: SignUpState): SignUpState {
        val isEmailValid = state.email.isNotBlank() &&
                Patterns.EMAIL_ADDRESS.matcher(state.email).matches()
        val isPasswordValid = state.password.length >= 6
        return state.copy(isSignUpEnabled = isEmailValid && isPasswordValid)
    }


}