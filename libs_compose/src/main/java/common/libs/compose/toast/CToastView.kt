/** Clone from https://github.com/dononcharles/NiceToast
 * from commit 91b458d
 */
package common.libs.compose.toast

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import common.libs.compose.R

/**
 * The core logic wrapper for the toast. It resolves styles, colors, and animations,
 * then delegates the actual rendering to [CToastFillComponent].
 */
@Composable
internal fun CToastView(
	data: CToastData,
	layoutType: CToastLayout = CToastLayout.Fill,
	config: CToastConfiguration = LocalCToastConfig.current
) {
	// 1. Resolve Style Specification
	val spec = remember(data.type) {
		config.getStyleSpec(data.type)
	}

	// 2. Resolve Colors
	val iconColor = colorResource(id = spec.iconColor)
	val backgroundColor = colorResource(id = spec.backgroundColor)

	val surfaceColor = if (isSystemInDarkTheme()) Color(0xFF121212) else Color.White
	val isDarkBg = backgroundColor.compositeOver(surfaceColor).luminance() < 0.5f

	val textColor = if (isDarkBg) {
		Color.White
	} else {
		Color.Black.copy(alpha = 0.9f)
	}

	// 3. Animation Logic
	val infiniteTransition = rememberInfiniteTransition(label = "icon_pulse_transition")
	val scale by infiniteTransition.animateFloat(
		initialValue = 1f,
		targetValue = 1.17f,
		animationSpec = infiniteRepeatable(
			animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
			repeatMode = RepeatMode.Reverse
		),
		label = "icon_pulse_scale"
	)

	// 4. Delegate Rendering
	when (layoutType) {
		CToastLayout.Fill ->
			CToastFillComponent(
				title = data.title ?: spec.defaultTitle,
				message = data.message,
				iconRes = spec.iconRes,
				iconColor = iconColor,
				backgroundColor = backgroundColor,
				textColor = textColor,
				iconScale = scale,
			)

		CToastLayout.Outlined ->
			CToastOutlinedComponent(
				title = data.title?:spec.defaultTitle,
				message = data.message,
				iconRes = spec.iconRes,
				iconColor = iconColor,
				textColor = textColor,
				iconScale = scale,
			)

		CToastLayout.Gradient ->
			CToastGradientComponent(
				title = data.title ?: spec.defaultTitle,
				message = data.message,
				iconRes = spec.iconRes,
				iconColor = iconColor,
				iconScale = scale,
			)
	}
}

/**
 * A stateless presentational component that renders the toast UI.
 */
@Composable
fun CToastFillComponent(
	title: String,
	message: String,
	@DrawableRes iconRes: Int,
	iconColor: Color,
	backgroundColor: Color,
	textColor: Color = Color.White,
	iconScale: Float
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		shape = RoundedCornerShape(dimensionResource(R.dimen.dimen_100)),
		elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
		colors = CardDefaults.cardColors(containerColor = backgroundColor)
	) {
		Row(
			modifier = Modifier.height(IntrinsicSize.Min),
			verticalAlignment = Alignment.CenterVertically
		) {
			// Icon Section
			Box(
				modifier = Modifier
					.padding(start = 14.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
					.graphicsLayer {
						scaleX = iconScale
						scaleY = iconScale
					}
					.clip(CircleShape)
					.background(Color.White.copy(alpha = 0.95f)),
				contentAlignment = Alignment.Center
			) {
				Icon(
					painter = painterResource(id = iconRes),
					contentDescription = title,
					tint = iconColor,
					modifier = Modifier
						.padding(8.dp)
						.size(20.dp)
				)
			}

			// Text Content Section
			Column(
				modifier = Modifier
					.weight(1f)
					.padding(vertical = 12.dp)
					.padding(end = 12.dp)
			) {
				Text(
					text = title,
					color = textColor,
					style = MaterialTheme.typography.titleSmall
				)
				Text(
					text = message,
					color = textColor,
					style = MaterialTheme.typography.bodySmall
				)
			}
		}
	}
}

/**
 * Display Style 2: Modern Outlined
 * Background is mostly surface/white with a colored border and accented icon.
 */
@Composable
fun CToastOutlinedComponent(
	title: String,
	message: String,
	@DrawableRes iconRes: Int,
	iconColor: Color,
	textColor: Color,
	iconScale: Float
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		shape = RoundedCornerShape(16.dp),
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
		border = BorderStroke(1.5.dp, iconColor.copy(alpha = 0.5f))
	) {
		Row(
			modifier = Modifier.height(IntrinsicSize.Min),
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(
				modifier = Modifier
					.padding(16.dp)
					.size(32.dp)
					.graphicsLayer {
						scaleX = iconScale
						scaleY = iconScale
					}
					.clip(CircleShape)
					.background(iconColor.copy(alpha = 0.1f)),
				contentAlignment = Alignment.Center
			) {
				Icon(
					painter = painterResource(id = iconRes),
					contentDescription = null,
					tint = iconColor,
					modifier = Modifier.size(20.dp)
				)
			}

			Column(
				modifier = Modifier
					.weight(1f)
					.padding(vertical = 12.dp)
					.padding(end = 16.dp)
			) {
				Text(text = title, color = iconColor, style = MaterialTheme.typography.titleMedium)
				Text(text = message, color = textColor, style = MaterialTheme.typography.bodySmall)
			}
		}
	}
}

/**
 * Display Style 3: Gradient Impact
 * Uses a lush gradient background for high visual impact.
 */
@Composable
fun CToastGradientComponent(
	title: String,
	message: String,
	@DrawableRes iconRes: Int,
	iconColor: Color,
	iconScale: Float
) {
	val gradient = Brush.horizontalGradient(
		colors = listOf(iconColor, iconColor.copy(alpha = 0.5f))
	)

	Card(
		modifier = Modifier.fillMaxWidth(),
		shape = RoundedCornerShape(12.dp),
	) {
		Row(
			modifier = Modifier
				.background(gradient)
				.height(IntrinsicSize.Min)
				.padding(12.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				painter = painterResource(id = iconRes),
				contentDescription = null,
				tint = Color.White,
				modifier = Modifier
					.size(28.dp)
					.graphicsLayer {
						scaleX = iconScale
						scaleY = iconScale
					}
			)

			Column(
				modifier = Modifier
					.weight(1f)
					.padding(start = 12.dp)
			) {
				Text(text = title, color = Color.White, style = MaterialTheme.typography.titleMedium)
				Text(text = message, color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodySmall)
			}
		}
	}
}
