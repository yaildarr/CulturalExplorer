package ru.ildar.domain.model

data class FeatureItem(
    val id: String,
    val titleResId: Int,
    val iconResId: Int,
    val onClick: () -> Unit
)
