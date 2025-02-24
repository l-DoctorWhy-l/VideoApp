package ru.rodipit.main_screen.ui

import androidx.compose.runtime.Immutable
import ru.rodipit.design.components.model.VideoItemUiData
import ru.rodipit.models.VideoModel
import ru.rodipit.utils.toFormattedTimeDuration

@Immutable
sealed interface MainScreenUiState {

    data class Loading(
        val isLoading: Boolean,
    ): MainScreenUiState

    data class Content(
        val videos: List<VideoItemUiData> = listOf(),
        val appending: Boolean = false,
    ): MainScreenUiState
}

internal fun VideoModel.toUi(): VideoItemUiData {
    return VideoItemUiData(
        id = this.id,
        name = this.title,
        thumbnail = this.thumbnail,
        duration = this.duration.toFormattedTimeDuration()
    )
}