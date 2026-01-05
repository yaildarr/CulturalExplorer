package ru.ildar.auth_impl.ui.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.ildar.api.usecase.SignUpUseCase
import ru.ildar.domain.model.MyResult


class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<SignUpViewState>(SignUpViewState())
    val viewState: StateFlow<SignUpViewState> = _viewState

    private val _state = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val state: StateFlow<SignUpState> = _state

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.EmailChanged -> {
                updateEmail(event.email)
                validateForm()
            }

            is SignUpEvent.PasswordChanged -> {
                updatePassword(event.password)
                validateForm()
            }

            SignUpEvent.SignUpClicked -> {
                signUp()
            }

            SignUpEvent.ResetError -> {
                resetError()
            }
        }
    }

    private fun updateEmail(email: String) {
        _viewState.update { it.copy(email = email) }
    }

    private fun updatePassword(password: String) {
        _viewState.update { it.copy(password = password) }
    }

    private fun validateForm() {
        val email = _viewState.value.email
        val password = _viewState.value.password

        val isEmailValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 6

        _viewState.update {
            it.copy(isSignUpEnabled = isEmailValid && isPasswordValid)
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            _state.value = SignUpState.Loading
            _viewState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = signUpUseCase.invoke(_viewState.value.email, _viewState.value.password)
            when (result){
                is MyResult.Error -> {
                    _state.value = SignUpState.Error(result.message)
                    _viewState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                is MyResult.Success -> {
                    _state.value = SignUpState.Success(result.userId)

                }

            }
            _viewState.update { it.copy(isLoading = false) }

        }
    }


    private fun resetError() {
        _state.value = SignUpState.Idle
        _viewState.update { it.copy(errorMessage = null) }
    }
}