package ru.ildar.auth_impl.di

import org.koin.core.module.dsl.viewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module
import ru.ildar.api.repository.AuthRepository
import ru.ildar.api.usecase.SignInUseCase
import ru.ildar.api.usecase.SignUpUseCase
import ru.ildar.auth_impl.data.AuthRepositoryImpl
import ru.ildar.auth_impl.ui.signin.SignInViewModel
import ru.ildar.auth_impl.ui.signup.SignUpViewModel

val authModule = module {

    // Firebase Auth
    single<FirebaseAuth> {
        FirebaseAuth.getInstance()
    }

    // Репозиторий
    single<AuthRepository> {
        AuthRepositoryImpl(firebaseAuth = get())
    }

    // Use Cases
    single { SignUpUseCase(repository = get()) }

    single { SignInUseCase(repository = get()) }

    viewModel {
        SignUpViewModel(signUpUseCase = get())
    }

    viewModel {
        SignInViewModel(signInUseCase = get())
    }

}