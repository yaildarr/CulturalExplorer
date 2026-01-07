package ru.ildar.auth_impl.ui.signup

sealed interface SignUpAction {
    data class EmailChanged(val email: String) : SignUpAction
    data class PasswordChanged(val password: String) : SignUpAction
    object SignUpClicked : SignUpAction
}

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignUpEnabled: Boolean = false,
    val isSuccess: Boolean = false

)

sealed interface SignUpSideEffect {
    object ShowSuccess: SignUpSideEffect
}