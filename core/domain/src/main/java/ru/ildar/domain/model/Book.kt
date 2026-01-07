package ru.ildar.domain.model

data class Book(
    val id: String,
    val title: String,
    val authors: List<String>,
    val firstPublishYear: Int?,
    val coverId: Long?,
    val language: List<String>?
)