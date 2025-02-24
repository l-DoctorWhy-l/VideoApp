package ru.rodipit.main_screen.viemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.rodipit.core.navigation.NavigationEvent
import ru.rodipit.core.navigation.Screen
import ru.rodipit.core.snackbar.SnackbarController
import ru.rodipit.core.snackbar.SnackbarEvent
import ru.rodipit.design.R
import ru.rodipit.main_screen.ui.MainScreenUiState
import ru.rodipit.main_screen.ui.toUi
import ru.rodipit.utils.StringResourceProvider
import ru.rodipit.utils.network.MyResult
import ru.rodipit.video_service.api.VideoRepository

internal class MainScreenViewModel(
    private val repository: VideoRepository,
    private val stringResourceProvider: StringResourceProvider,
) : ViewModel() {

    private var loadingJob: Job? = null
    private var nextPage: String? = null

    private val _uiState: MutableStateFlow<MainScreenUiState> = MutableStateFlow(
        MainScreenUiState.Loading(isLoading = true)
    )
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent: MutableSharedFlow<NavigationEvent?> = MutableSharedFlow()
    val navigationEvent = _navigationEvent.asSharedFlow()


    private val _refreshingState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val refreshingState = _refreshingState.asStateFlow()

    init {
        loadVideos()
    }

    fun onRefresh() {
        loadingJob?.cancel()
        loadingJob = null
        _refreshingState.value = true
        loadingJob = viewModelScope.launch {

            when (val result = repository.loadVideos(source = null)) {
                is MyResult.Success -> {
                    _refreshingState.value = false
                    _uiState.value = MainScreenUiState.Content(
                        videos = result.data.videos.map { it.toUi() },
                    )
                    nextPage = result.data.nextPage
                }

                is MyResult.Error, is MyResult.ErrorWithContent -> {
                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            name = stringResourceProvider.getString(R.string.refreshing_failed_message)
                        )
                    )
                    if (_uiState.value is MainScreenUiState.Loading) {
                        _uiState.value = MainScreenUiState.Loading(false)
                    }
                    _refreshingState.value = false
                }
            }
        }
    }

    fun loadVideos() {
        if (loadingJob?.isActive == true) return

        loadingJob = viewModelScope.launch {
            when (val curState = _uiState.value) {
                is MainScreenUiState.Loading -> {
                    when (val result = repository.loadVideos(source = null)) {
                        is MyResult.Success -> {
                            _refreshingState.value = false
                            _uiState.value = MainScreenUiState.Content(
                                videos = result.data.videos.map { it.toUi() },
                            )
                            nextPage = result.data.nextPage
                        }

                        is MyResult.Error -> {
                            result.exception.message?.let { message ->
                                SnackbarController.sendEvent(SnackbarEvent(message))
                            }
                            if (_uiState.value is MainScreenUiState.Loading) {
                                _uiState.value = MainScreenUiState.Loading(false)
                            }
                            _refreshingState.value = false
                        }

                        is MyResult.ErrorWithContent -> {
                            _refreshingState.value = false
                            result.exception.message?.let { message ->
                                SnackbarController.sendEvent(SnackbarEvent(message))
                            }
                            _uiState.value = MainScreenUiState.Content(
                                videos = result.data.videos.map { it.toUi() },
                            )
                        }
                    }
                }

                is MainScreenUiState.Content -> {
                    _uiState.value = curState.copy(appending = true)
                    when (val result = repository.loadVideos(nextPage)) {
                        is MyResult.Success -> {
                            _uiState.value = curState.copy(
                                videos = curState.videos + result.data.videos.map { it.toUi() },
                                appending = false,
                            )
                            nextPage = result.data.nextPage
                        }

                        is MyResult.ErrorWithContent -> {
                            result.exception.message?.let { message ->
                                SnackbarController.sendEvent(SnackbarEvent(message))
                            }
                        }

                        is MyResult.Error -> {
                            result.exception.message?.let { message ->
                                SnackbarController.sendEvent(SnackbarEvent(message))
                            }
                            _uiState.value = curState.copy(appending = false)
                        }
                    }
                }
            }
        }
    }

    fun navigateToVideoScreen(position: Int) {
        (_uiState.value as? MainScreenUiState.Content)?.let {
            viewModelScope.launch {
                _navigationEvent.emit(
                    NavigationEvent.NavigateTo(
                        Screen.Video(
                            id = it.videos[position].id,
                        )
                    )
                )
            }
        }
    }

}