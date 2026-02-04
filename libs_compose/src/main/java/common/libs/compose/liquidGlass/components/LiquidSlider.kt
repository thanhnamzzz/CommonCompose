package common.libs.compose.liquidGlass.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.util.fastRoundToInt
import androidx.compose.ui.util.lerp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberBackdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.shadow.InnerShadow
import com.kyant.backdrop.shadow.Shadow
import common.libs.compose.liquidGlass.utils.DampedDragAnimation
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LiquidSlider(
	modifier: Modifier = Modifier,
	backdrop: Backdrop,
	value: () -> Float,
	onValueChange: (Float) -> Unit,
	valueRange: ClosedFloatingPointRange<Float>,
	visibilityThreshold: Float = 0.01f,
	trackHeight: Dp = 6f.dp,
	shapeTrack: Shape = RoundedCornerShape(50.dp),
	thumbHeight: Dp = 24.dp,
	thumbWidth: Dp = 40.dp,
	shapeThumb: Shape = RoundedCornerShape(50.dp),
	thumbColor: Color = Color.White,
	thumbColorShadow: Color = Color.Black,
	accentColor: Color = Color(0xFF0088FF),
	trackColor: Color = Color(0xFF787878).copy(0.36f),
	scaleInit: Float = 1f,
	scalePressed: Float = 1.5f
) {
//    val isLightTheme = !isSystemInDarkTheme()
//    val accentColor =
//        if (isLightTheme) Color(0xFF0088FF)
//        else Color(0xFF0091FF)
//    val trackColor =
//        if (isLightTheme) Color(0xFF787878).copy(0.2f)
//        else Color(0xFF787880).copy(0.36f)

	val trackBackdrop = rememberLayerBackdrop()

	BoxWithConstraints(
		modifier.fillMaxWidth(),
		contentAlignment = Alignment.CenterStart
	) {
		val trackWidth = constraints.maxWidth

		val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
		val animationScope = rememberCoroutineScope()
		var didDrag by remember { mutableStateOf(false) }
		val dampedDragAnimation = remember(animationScope) {
			DampedDragAnimation(
				animationScope = animationScope,
				initialValue = value(),
				valueRange = valueRange,
				visibilityThreshold = visibilityThreshold,
				initialScale = scaleInit,
				pressedScale = scalePressed,
				onDragStarted = {},
				onDragStopped = {
					if (didDrag) {
						onValueChange(targetValue)
					}
				},
				onDrag = { _, dragAmount ->
					if (!didDrag) {
						didDrag = dragAmount.x != 0f
					}
					val delta =
						(valueRange.endInclusive - valueRange.start) * (dragAmount.x / trackWidth)
					onValueChange(
						if (isLtr) (targetValue + delta).coerceIn(valueRange)
						else (targetValue - delta).coerceIn(valueRange)
					)
				}
			)
		}
		LaunchedEffect(dampedDragAnimation) {
			snapshotFlow { value() }
				.collectLatest { value ->
					if (dampedDragAnimation.targetValue != value) {
						dampedDragAnimation.updateValue(value)
					}
				}
		}

		//Track
		Box(Modifier.layerBackdrop(trackBackdrop)) {
			Box(
				Modifier
					.clip(shapeTrack)
					.background(trackColor)
					.pointerInput(animationScope) {
						detectTapGestures { position ->
							val delta =
								(valueRange.endInclusive - valueRange.start) * (position.x / trackWidth)
							val targetValue =
								(if (isLtr) valueRange.start + delta
								else valueRange.endInclusive - delta)
									.coerceIn(valueRange)
							dampedDragAnimation.animateToValue(targetValue)
							onValueChange(targetValue)
						}
					}
					.height(trackHeight)
					.fillMaxWidth()
			)

			Box(
				Modifier
					.clip(shapeTrack)
					.background(accentColor)
					.height(trackHeight)
					.layout { measurable, constraints ->
						val placeable = measurable.measure(constraints)
						val width =
							(constraints.maxWidth * dampedDragAnimation.progress).fastRoundToInt()
						layout(width, placeable.height) {
							placeable.place(0, 0)
						}
					}
			)
		}

		//Thumb
		Box(
			Modifier
				.graphicsLayer {
					translationX =
						(-size.width / 2f + trackWidth * dampedDragAnimation.progress)
							.fastCoerceIn(
								-size.width / 4f,
								trackWidth - size.width * 3f / 4f
							) * if (isLtr) 1f else -1f
				}
				.then(dampedDragAnimation.modifier)
				.drawBackdrop(
					backdrop = rememberCombinedBackdrop(
						backdrop,
						rememberBackdrop(trackBackdrop) { drawBackdrop ->
							val progress = dampedDragAnimation.pressProgress
							val scaleX = lerp(2f / 3f, 1f, progress)
							val scaleY = lerp(0f, 1f, progress)
							scale(scaleX, scaleY) {
								drawBackdrop()
							}
						}
					),
					shape = { shapeThumb },
					effects = {
						val progress = dampedDragAnimation.pressProgress
						blur(8f.dp.toPx() * (1f - progress))
						lens(
							10f.dp.toPx() * progress,
							14f.dp.toPx() * progress,
							chromaticAberration = true
						)
					},
					highlight = {
						val progress = dampedDragAnimation.pressProgress
						Highlight.Ambient.copy(
							width = Highlight.Ambient.width / 1.5f,
							blurRadius = Highlight.Ambient.blurRadius / 1.5f,
							alpha = progress
						)
					},
					shadow = {
						Shadow(
							radius = 4f.dp,
							color = thumbColorShadow.copy(alpha = 0.05f)
						)
					},
					innerShadow = {
						val progress = dampedDragAnimation.pressProgress
						InnerShadow(
							radius = 4f.dp * progress,
							alpha = progress
						)
					},
					layerBlock = {
						scaleX = dampedDragAnimation.scaleX
						scaleY = dampedDragAnimation.scaleY
						val velocity = dampedDragAnimation.velocity / 10f
						scaleX /= 1f - (velocity * 0.75f).fastCoerceIn(-0.2f, 0.2f)
						scaleY *= 1f - (velocity * 0.25f).fastCoerceIn(-0.2f, 0.2f)
					},
					onDrawSurface = {
						val progress = dampedDragAnimation.pressProgress
						drawRect(thumbColor.copy(alpha = 1f - progress))
					}
				)
				.size(thumbWidth, thumbHeight)
		)
	}
}
