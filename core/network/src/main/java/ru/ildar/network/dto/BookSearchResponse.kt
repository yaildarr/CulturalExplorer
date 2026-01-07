package ru.ildar.network.dto

import com.google.gson.annotations.SerializedName


data class BookSearchResponse(
    @SerializedName("docs") val docs: List<BookDto>
)

data class BookDto(
    @SerializedName("key") val key: String,
    @SerializedName("title") val title: String,
    @SerializedName("author_name") val authorName: List<String>? = null,
    @SerializedName("first_publish_year") val firstPublishYear: Int? = null,
    @SerializedName("cover_i") val coverId: Long? = null,
    @SerializedName("language") val language: List<String>? = null
)