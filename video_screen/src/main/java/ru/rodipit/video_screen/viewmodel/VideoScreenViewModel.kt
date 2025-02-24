package ru.rodipit.video_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import ru.rodipit.core.navigation.NavigationEvent
import ru.rodipit.core.snackbar.SnackbarController
import ru.rodipit.core.snackbar.SnackbarEvent
import ru.rodipit.utils.StringResourceProvider
import ru.rodipit.utils.network.MyResult
import ru.rodipit.design.R
import ru.rodipit.video_screen.ui.VideoScreenUiData
import ru.rodipit.video_service.api.VideoRepository

internal class VideoScreenViewModel(
    private val videoId: String,
    private val repository: VideoRepository,
    private val stringResourceProvider: StringResourceProvider,
    val player: Player,
) : ViewModel() {

    private val _uiState: MutableStateFlow<VideoScreenUiData> = MutableStateFlow(
        VideoScreenUiData.Loading(isLoading = true)
    )
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent: MutableSharedFlow<NavigationEvent?> = MutableSharedFlow()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadVideo()
    }

    private fun loadVideo() {
        viewModelScope.launch {

            when (val smallVideo = repository.loadVideo(id = videoId)) {
                is MyResult.Success -> {
                    _uiState.value = VideoScreenUiData.Content(title = smallVideo.data.title)
                    player.prepare()
                    player.setMediaItem(MediaItem.fromUri(smallVideo.data.sourceLink))
                    player.playWhenReady = true
                }

                is MyResult.Error, is MyResult.ErrorWithContent -> {
                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            name = stringResourceProvider.getString(R.string.loading_failed_message)
                        )
                    )
                    _uiState.value = VideoScreenUiData.Loading(isLoading = false)
                }
            }
        }
    }

    fun onBackPressed() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.PopBackStack)
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

}

internal class VideoScreenViewModelFactory(
    private val videoId: String,
) : ViewModelProvider.Factory {

    private val player: Player by inject(Player::class.java)
    private val repository: VideoRepository by inject(VideoRepository::class.java)
    private val stringResourceProvider: StringResourceProvider by inject(StringResourceProvider::class.java)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoScreenViewModel::class.java)) {
            return VideoScreenViewModel(
                videoId = videoId,
                player = player,
                repository = repository,
                stringResourceProvider = stringResourceProvider,
            ) as T
        }
        return super.create(modelClass)
    }
}