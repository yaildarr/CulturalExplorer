package ru.ildar.api.repository

import ru.ildar.domain.model.AuthResult

interface AuthRepository {

    suspend fun signUp(email: String, password: String) : AuthResult

    suspend fun isAuthenticated() : Boolean

    suspend fun signIn(email: String, password: String) : AuthResult
}