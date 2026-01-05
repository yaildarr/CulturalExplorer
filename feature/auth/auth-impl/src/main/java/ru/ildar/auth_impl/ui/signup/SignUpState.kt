package ru.ildar.auth_impl.ui.signup

sealed class SignUpState {
    data object Idle : SignUpState()
    data object Loading : SignUpState()
    data class Success(val uid: String) : SignUpState()
    data class Error(val message: String) : SignUpState()
}

sealed class SignUpEvent {
    data class EmailChanged(val email: String) : SignUpEvent()
    data class PasswordChanged(val password: String) : SignUpEvent()
    object SignUpClicked : SignUpEvent()
    object ResetError : SignUpEvent()
}

data class SignUpViewState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignUpEnabled: Boolean = false

)