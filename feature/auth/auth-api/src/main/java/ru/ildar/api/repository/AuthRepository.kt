package ru.ildar.api.repository

import ru.ildar.domain.model.MyResult

interface AuthRepository {

    suspend fun signUp(email: String, password: String) : MyResult

    suspend fun isAuthenticated() : Boolean
}