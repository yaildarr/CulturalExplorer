package ru.ildar.domain.model

sealed class MyResult<out T> {
    data class Success<T>(val data: T) : MyResult<T>()
    data class Error(val message: String) : MyResult<Nothing>()
}