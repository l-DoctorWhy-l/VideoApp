package ru.rodipit.design.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import ru.rodipit.design.components.model.VideoItemUiData
import ru.rodipit.design.shimmers.SingleLineTextShimmer
import ru.rodipit.design.shimmers.shimmerEffect
import ru.rodipit.design.theme.AppTheme


@Composable
fun VideoItem(
    uiData: VideoItemUiData,
    onVideoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onVideoClick)
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clip(RoundedCornerShape(8.dp)),
            model = uiData.thumbnail,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            loading = {
                Box(Modifier.fillMaxSize().shimmerEffect(true))
            },
            error = {
                Box(Modifier.fillMaxSize().shimmerEffect(false))
            }
        )
        Text(
            text = uiData.name,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            color = MaterialTheme.colorScheme.onPrimary,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )
        Text(
            text = uiData.duration,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun VideoItemShimmer(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp),
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect(isLoading),
        )
        SingleLineTextShimmer(
            modifier = Modifier.padding(8.dp),
            width = 120.dp,
            isLoading = isLoading,
            textStyle = MaterialTheme.typography.bodyLarge,
        )
        SingleLineTextShimmer(
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 4.dp),
            width = 60.dp,
            isLoading = isLoading,
            textStyle = MaterialTheme.typography.bodyLarge,
        )
    }
}





@Preview
@Composable
private fun VideoItemPreview() {
    AppTheme {
        Column {
            VideoItem(
                uiData = VideoItemUiData.forPreview(),
                onVideoClick = { },
            )
            Spacer(Modifier.height(8.dp))
            VideoItemShimmer(
                isLoading = true,
            )
        }
    }
}
