package ru.ildar.api.usecase

import ru.ildar.api.repository.AuthRepository
import ru.ildar.domain.model.MyResult

class SignUpUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): MyResult {
        return repository.signUp(email, password)
    }
}