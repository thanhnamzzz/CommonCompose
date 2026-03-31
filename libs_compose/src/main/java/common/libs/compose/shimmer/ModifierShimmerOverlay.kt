package common.libs.compose.shimmer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Example of a custom shimmer, in case different parameters, as for example the animation
 * duration, would be more convenient.
 */
fun Modifier.shimmerOverlay(
    duration: Int = 1800,
    delayMillis: Int = 400,
    rotation: Float = 25f,
    blendMode: BlendMode = BlendMode.DstIn,
    shaderColors: List<Color> = listOf(
        Color.Unspecified.copy(alpha = 1.0f),
        Color.Unspecified.copy(alpha = 0.3f),
        Color.Unspecified.copy(alpha = 1.0f),
    ),
    shimmerWidth: Dp = 100.dp,
    shimmerBounds: ShimmerBounds = ShimmerBounds.View,
): Modifier = composed {
    val shimmer = rememberShimmer(
        shimmerBounds = shimmerBounds,
        theme = createCustomTheme(
            duration = duration,
            delayMillis = delayMillis,
            rotation = rotation,
            blendMode = blendMode,
            shaderColors = shaderColors,
            shimmerWidth = shimmerWidth,
        ),
    )
    shimmer(customShimmer = shimmer)
}

private fun createCustomTheme(
    duration: Int = 1800,
    delayMillis: Int = 400,
    rotation: Float = 25f,
    blendMode: BlendMode = BlendMode.DstIn,
    shaderColors: List<Color> = listOf(
        Color.Unspecified.copy(alpha = 1.0f),
        Color.Unspecified.copy(alpha = 0.3f),
        Color.Unspecified.copy(alpha = 1.0f),
    ),
    shimmerWidth: Dp = 100.dp,
) = defaultShimmerTheme.copy(
    animationSpec = infiniteRepeatable(
        animation = shimmerSpec(
            durationMillis = duration,
            delayMillis = delayMillis,
            easing = LinearEasing,
        ),
        repeatMode = RepeatMode.Restart,
    ),
    blendMode = blendMode,
    rotation = rotation,
//    shaderColors = listOf(
//        Color.Unspecified.copy(alpha = 1.0f),
//        Color.White.copy(alpha = 0.3f),
//        Color.Unspecified.copy(alpha = 1.0f),
//    ),
    shaderColors = shaderColors,
    shaderColorStops = null,
    shimmerWidth = shimmerWidth,
)
