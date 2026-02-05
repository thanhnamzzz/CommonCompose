package common.libs.compose.liquidGlass.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceIn
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
fun LiquidToggle(
    modifier: Modifier = Modifier,
    backdrop: Backdrop,
	selected: () -> Boolean,
	onSelect: (Boolean) -> Unit,
    onClickToggle: () -> Unit = {},
	accentColor: Color = Color(0xFF34C759),
	trackColor: Color = Color(0xFF787878).copy(0.36f),
	thumbColor: Color = Color.White,
	thumbColorShadow: Color = Color.Black,
	shapeTrack: Shape = RoundedCornerShape(50.dp),
	shapeThumb: Shape = RoundedCornerShape(50.dp),
    paddingTrack: Dp = 2.dp,
	trackHeigh: Dp = 28.dp,
	trackWidth: Dp = 64.dp,
	thumbHeight: Dp = 24.dp,
	thumbWidth: Dp = 40.dp,
	//dragDistance là quãng đường di chuyển của thumb = trackWidth - 2 * paddingTrack - thumbWidth
	dragDistance: Dp = 20.dp,
	visibilityThreshold: Float = 0.01f,
	scaleInit: Float = 1f,
	scalePressed: Float = 1.5f
) {
//	val isLightTheme = !isSystemInDarkTheme()
//	val accentColor =
//		if (isLightTheme) Color(0xFF34C759)
//		else Color(0xFF30D158)
//	val trackColor =
//		if (isLightTheme) Color(0xFF787878).copy(0.2f)
//		else Color(0xFF787880).copy(0.36f)

	val density = LocalDensity.current
	val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
	val dragWidth = with(density) { dragDistance.toPx() }
	val animationScope = rememberCoroutineScope()
	var didDrag by remember { mutableStateOf(false) }
	var fraction by remember { mutableFloatStateOf(if (selected()) 1f else 0f) }
	val dampedDragAnimation = remember(animationScope) {
		DampedDragAnimation(
			animationScope = animationScope,
			initialValue = fraction,
			valueRange = 0f..1f,
			visibilityThreshold = visibilityThreshold,
			initialScale = scaleInit,
			pressedScale = scalePressed,
			onDragStarted = {},
			onDragStopped = {
				if (didDrag) {
					fraction = if (targetValue >= 0.5f) 1f else 0f
					onSelect(fraction == 1f)
					didDrag = false
				} else {
					fraction = if (selected()) 0f else 1f
					onSelect(fraction == 1f)
				}
			},
			onDrag = { _, dragAmount ->
				if (!didDrag) {
					didDrag = dragAmount.x != 0f
				}
				val delta = dragAmount.x / dragWidth
				fraction =
					if (isLtr) (fraction + delta).fastCoerceIn(0f, 1f)
					else (fraction - delta).fastCoerceIn(0f, 1f)
			}
		)
	}
	LaunchedEffect(dampedDragAnimation) {
		snapshotFlow { fraction }
			.collectLatest { fraction ->
				dampedDragAnimation.updateValue(fraction)
			}
	}
	LaunchedEffect(selected) {
		snapshotFlow { selected() }
			.collectLatest { isSelected ->
				val target = if (isSelected) 1f else 0f
				if (target != fraction) {
					fraction = target
					dampedDragAnimation.animateToValue(target)
				}
			}
	}

	val trackBackdrop = rememberLayerBackdrop()

	Box(
		modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null, // This removes the ripple effect
                enabled = true,
                onClick = onClickToggle
            ),
		contentAlignment = Alignment.CenterStart
	) {
		//Track
		Box(
			Modifier
				.layerBackdrop(trackBackdrop)
				.clip(shapeTrack)
				.drawBehind {
					val fraction = dampedDragAnimation.value
					drawRect(lerp(trackColor, accentColor, fraction))
				}
				.size(trackWidth, trackHeigh)
		)

		//Thumb
		Box(
			Modifier
				.graphicsLayer {
					val fraction = dampedDragAnimation.value
					val padding = paddingTrack.toPx()
					translationX =
						if (isLtr) lerp(padding, padding + dragWidth, fraction)
						else lerp(-padding, -(padding + dragWidth), fraction)
				}
				.semantics {
					role = Role.Switch
				}
				.then(dampedDragAnimation.modifier)
				.drawBackdrop(
					backdrop = rememberCombinedBackdrop(
						backdrop,
						rememberBackdrop(trackBackdrop) { drawBackdrop ->
							val progress = dampedDragAnimation.pressProgress
							val scaleX = lerp(2f / 3f, 0.75f, progress)
							val scaleY = lerp(0f, 0.75f, progress)
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
							5f.dp.toPx() * progress,
							10f.dp.toPx() * progress,
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
						val velocity = dampedDragAnimation.velocity / 50f
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
