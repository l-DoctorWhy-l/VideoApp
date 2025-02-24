package ru.rodipit.design.shimmers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.rodipit.design.shimmers.shimmerEffect

@Composable
fun SingleLineTextShimmer(
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    width: Dp = 40.dp,
    isLoading: Boolean = true,
) {

    val textMeasurer = rememberTextMeasurer()

    val textHeightPx = remember { textMeasurer.measure("A", textStyle).size.height }

    val lineHeightDp = with(LocalDensity.current) { textHeightPx.toDp() }

    Box(
        modifier = modifier
            .width(width)
            .height(lineHeightDp)
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 2.dp)
            .shimmerEffect(isLoading = isLoading)
    ) {  }
}