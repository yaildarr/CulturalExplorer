package ru.ildar.domain.model

sealed class MyResult {
    data class Success(val userId: String) : MyResult()
    data class Error(val message: String) : MyResult()
}