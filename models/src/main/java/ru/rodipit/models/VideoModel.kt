package ru.rodipit.models

data class VideoModel(
    val id: String,
    val title: String,
    val duration: Long,
    val sourceLink: String,
    val thumbnail: String,
)
