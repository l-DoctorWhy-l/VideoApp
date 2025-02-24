package ru.rodipit.core.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Main: Screen
    @Serializable
    data class Video(val id: String): Screen

}