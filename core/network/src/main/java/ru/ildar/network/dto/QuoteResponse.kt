package ru.ildar.network.dto

import com.google.gson.annotations.SerializedName

data class QuoteResponse(
    @SerializedName("_id") val id: String,
    @SerializedName("content") val content: String,
    @SerializedName("author") val author: String
)
