package ru.ildar.auth_impl.ui.signin

sealed interface SignInAction {
    data class EmailChanged(val email: String) : SignInAction
    data class PasswordChanged(val password: String) : SignInAction
    object SignInClicked : SignInAction
}

data class SignInState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignInEnabled: Boolean = false,
    val isSuccess: Boolean = false

)

sealed interface SignInSideEffect {
    object ShowSuccess: SignInSideEffect
}