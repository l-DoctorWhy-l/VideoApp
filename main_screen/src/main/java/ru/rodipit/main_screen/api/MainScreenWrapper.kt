package ru.rodipit.main_screen.api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.rodipit.core.navigation.NavigationEvent
import ru.rodipit.main_screen.ui.MainScreenContent
import ru.rodipit.main_screen.viemodel.MainScreenViewModel
import ru.rodipit.utils.compose.ObserveAsEvents

@Composable
fun MainScreenWrapper(
    navController: NavController,
) {
    val viewModel = koinViewModel<MainScreenViewModel>()

    ObserveAsEvents(flow = viewModel.navigationEvent) { event ->
        when (event) {
            is NavigationEvent.NavigateTo -> navController.navigate(event.screen)
            is NavigationEvent.PopBackStack -> navController.popBackStack()
            null -> Unit
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        MainScreenContent(
            uiState = viewModel.uiState.collectAsState(),
            refreshingState = viewModel.refreshingState.collectAsState(),
            onLoadNextItems = viewModel::loadVideos,
            onRefresh = viewModel::onRefresh,
            onNavigateToVideoScreen = viewModel::navigateToVideoScreen,
        )
    }
}