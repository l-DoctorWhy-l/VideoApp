package ru.rodipit.main_screen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.rodipit.design.components.VideoItem
import ru.rodipit.design.components.VideoItemShimmer
import ru.rodipit.design.components.model.VideoItemUiData
import ru.rodipit.design.theme.AppTheme


private const val PREFETCHING_NUMBER = 4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreenContent(
    uiState: State<MainScreenUiState>,
    refreshingState: State<Boolean>,
    onLoadNextItems: () -> Unit,
    onRefresh: () -> Unit,
    onNavigateToVideoScreen: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = refreshingState.value,
        onRefresh = onRefresh,
        state = pullRefreshState,
    ) {
        when(val uiData = uiState.value) {
            is MainScreenUiState.Loading -> {
                MainScreenLoading(isLoading = uiData.isLoading)
            }
            is MainScreenUiState.Content -> {
                MainScreen(
                    uiState = uiData,
                    onLoadNextItems = onLoadNextItems,
                    onNavigateToVideoScreen = onNavigateToVideoScreen,
                    modifier = modifier,
                )
            }
        }
    }

}


@Composable
private fun MainScreen(
    uiState: MainScreenUiState.Content,
    onLoadNextItems: () -> Unit,
    onNavigateToVideoScreen: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    val lazyListState = rememberLazyListState()

    val firstVisibleItem by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex
        }
    }

    LaunchedEffect(firstVisibleItem) {
        if (firstVisibleItem >= uiState.videos.lastIndex - PREFETCHING_NUMBER){
            onLoadNextItems()
        }
    }

    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        itemsIndexed(
            items = uiState.videos,
            key = { _, item ->
                item.id
            }
        ) { index,  videoItem ->
            VideoItem(
                uiData = videoItem,
                onVideoClick = {
                    onNavigateToVideoScreen(index)
                }
            )
        }
        if (uiState.appending) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun MainScreenLoading(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        items(2) {
            VideoItemShimmer(isLoading = isLoading)
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    AppTheme {
        MainScreenContent(
            uiState = remember {
                mutableStateOf(
                    MainScreenUiState.Content(
                        videos = listOf(VideoItemUiData.forPreview()),
                        appending = false,
                    )
                )
            },
            refreshingState = remember { mutableStateOf(false) },
            onRefresh = { },
            onLoadNextItems = { },
            onNavigateToVideoScreen = { },
        )
    }
}