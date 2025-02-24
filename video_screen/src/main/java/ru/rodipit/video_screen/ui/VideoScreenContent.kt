package ru.rodipit.video_screen.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay
import ru.rodipit.design.shimmers.SingleLineTextShimmer
import ru.rodipit.design.shimmers.shimmerEffect


private const val RESTORE_CONFIGURATION_SETTINGS_FROM_LANDSCAPE_DELAY = 1000L
private const val RESTORE_CONFIGURATION_SETTINGS_FROM_PORTRAIT_DELAY = 2000L

@OptIn(UnstableApi::class)
@Composable
internal fun VideoScreenContent(
    uiState: State<VideoScreenUiData>,
    videoPlayer: MutableState<Player>,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {

    when(val uiData = uiState.value) {
        is VideoScreenUiData.Loading -> {
            VideoScreenLoading(
                isLoading = uiData.isLoading,
                modifier = modifier,
            )
        }
        is VideoScreenUiData.Content -> {
            VideoScreen(
                uiData = uiData,
                videoPlayer = videoPlayer,
                onBackPressed = onBackPressed,
                modifier = modifier,
            )
        }
    }


}

@OptIn(UnstableApi::class)
@Composable
private fun VideoScreen(
    uiData: VideoScreenUiData.Content,
    videoPlayer: MutableState<Player>,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val localContext = LocalContext.current
    val configuration = LocalConfiguration.current
    val activity = remember { (localContext as Activity) }
    var fullScreenModeEnabled by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(fullScreenModeEnabled) {
        when (fullScreenModeEnabled) {
            true -> {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
                delay(RESTORE_CONFIGURATION_SETTINGS_FROM_LANDSCAPE_DELAY)
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
            }
            false -> {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
                delay(RESTORE_CONFIGURATION_SETTINGS_FROM_PORTRAIT_DELAY)
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
            }
        }
    }

    LaunchedEffect(configuration.orientation) {
        when(configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> fullScreenModeEnabled = false
            Configuration.ORIENTATION_LANDSCAPE -> fullScreenModeEnabled = true
            else -> Unit
        }
    }

    BackHandler {
        if (fullScreenModeEnabled) {
            fullScreenModeEnabled = false
        } else {
            onBackPressed()
        }
    }

    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = videoPlayer.value
                    useController = true
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    setFullscreenButtonClickListener {
                        fullScreenModeEnabled = !fullScreenModeEnabled
                    }
                }
            },
            update = {
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        it.onPause()
                        it.player?.pause()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        it.onResume()
                    }
                    else -> Unit
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .thenIf(Modifier.aspectRatio(16 / 9f)) { !fullScreenModeEnabled }
                .thenIf(Modifier.fillMaxHeight()) { fullScreenModeEnabled }
                .clip(RoundedCornerShape(8.dp))
        )
        Text(
            text = uiData.title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )
    }
}

fun Modifier.thenIf(modifier: Modifier, expression: () -> Boolean): Modifier {
    return if (expression.invoke()) this.then(modifier) else this
}

@Composable
private fun VideoScreenLoading(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect(isLoading = isLoading)
        )
        SingleLineTextShimmer(
            textStyle = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .width(200.dp)
                .padding(8.dp),
        )
    }
}