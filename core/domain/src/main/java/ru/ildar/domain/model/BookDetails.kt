package ru.ildar.domain.model

data class BookDetails(
    val id: String,
    val title: String,
    val description: String?,
    val coverId: Long?,
    val firstPublishYear: String?,
    val subjects: List<String>
)
