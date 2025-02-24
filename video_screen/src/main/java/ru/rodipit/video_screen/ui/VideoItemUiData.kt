package ru.rodipit.video_screen.ui

import androidx.compose.runtime.Stable

@Stable
sealed interface VideoScreenUiData {

    data class Loading(
        val isLoading: Boolean,
    ): VideoScreenUiData

    data class Content(
        val title: String,
    ): VideoScreenUiData

}
