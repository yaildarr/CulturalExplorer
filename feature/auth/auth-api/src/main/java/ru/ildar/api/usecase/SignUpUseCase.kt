package ru.ildar.api.usecase

import ru.ildar.api.repository.AuthRepository
import ru.ildar.domain.model.AuthResult

class SignUpUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        return repository.signUp(email, password)
    }
}