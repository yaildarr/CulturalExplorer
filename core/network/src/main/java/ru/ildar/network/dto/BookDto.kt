package ru.ildar.network.dto

import com.google.gson.stream.JsonReader
import ru.ildar.domain.model.BookDetails
import ru.ildar.network.R
import ru.ildar.network.dto.DescriptionDto.Text

data class WorkDetailsDto(
    val title: String = "Без названия",
    val description: DescriptionDto? = Text("Описания нет"),
    val covers: List<Long>? = emptyList(),
    val first_publish_date: String? = "Нет информации",
    val subjects: List<String>? = emptyList()
)

sealed class DescriptionDto {
    data class Text(val value: String) : DescriptionDto()
    data class Raw(val text: String) : DescriptionDto()
}

fun WorkDetailsDto.toDomain(id: String): BookDetails {
    return BookDetails(
        id = id,
        title = title,
        description = when (description) {
            is DescriptionDto.Text -> description.value
            is DescriptionDto.Raw -> description.text
            null -> null
        },
        coverId = covers?.firstOrNull(),
        firstPublishYear = first_publish_date,
        subjects = subjects.orEmpty()
    )
}
