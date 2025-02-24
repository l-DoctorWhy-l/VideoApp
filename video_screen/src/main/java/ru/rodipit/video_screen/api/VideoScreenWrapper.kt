package ru.rodipit.video_screen.api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.rodipit.core.navigation.NavigationEvent
import ru.rodipit.video_screen.ui.VideoScreenContent
import ru.rodipit.video_screen.viewmodel.VideoScreenViewModel
import ru.rodipit.video_screen.viewmodel.VideoScreenViewModelFactory

@Composable
fun VideoScreenWrapper(
    videoId: String,
    navController: NavController,
) {
    val viewModel: VideoScreenViewModel = viewModel(
        factory = VideoScreenViewModelFactory(
            videoId = videoId,
        )
    )

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> navController.navigate(event.screen)
                is NavigationEvent.PopBackStack -> navController.popBackStack()
                null -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        VideoScreenContent(
            uiState = viewModel.uiState.collectAsState(),
            videoPlayer = remember { mutableStateOf(viewModel.player) },
            onBackPressed = viewModel::onBackPressed,
        )
    }
}