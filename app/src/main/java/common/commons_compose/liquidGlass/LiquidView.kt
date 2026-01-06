package common.commons_compose.liquidGlass

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.backdrops.rememberCanvasBackdrop
import common.commons_compose.R
import common.commons_compose.liquidGlass.components.LiquidButton
import common.commons_compose.liquidGlass.components.LiquidSlider
import common.commons_compose.liquidGlass.components.LiquidToggle
import common.libs.compose.toast.CToastConfiguration
import common.libs.compose.toast.CToastHost
import common.libs.compose.toast.CToastState
import common.libs.compose.toast.CToastType
import common.libs.compose.toast.LocalCToastConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LiquidView(
	modifier: Modifier = Modifier,
	context: Context
) {
	val cToastState = remember { CToastState() }
	val scope = rememberCoroutineScope()
	var selected by rememberSaveable { mutableStateOf(false) }

	val isLightTheme = !isSystemInDarkTheme()
	val backgroundColor =
		if (isLightTheme) Color(0xFFFFFFFF)
		else Color(0xFF121212)

	var value by rememberSaveable { mutableFloatStateOf(50f) }

	LiquidViewScaffold(
		modifier = modifier.fillMaxSize(),
		background = R.mipmap.wallpaper_light
	) { liquidDrop ->
		CompositionLocalProvider(LocalCToastConfig provides CToastConfiguration()) {
			Box {
				Column(
					modifier = modifier.fillMaxSize(),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Center
				) {
					Text(
						text = "Liquid glass Components",
						style = TextStyle(Color(0xFFE91E63), 20f.sp, FontWeight.Medium)
					)
					Spacer(
						Modifier
							.fillMaxWidth()
							.height(10.dp)
					)
					LiquidButton(
						onClick = { showToast(scope, cToastState, context, "1") },
						backdrop = liquidDrop
					) { Text("Liquid Button 1") }
					Spacer(
						Modifier
							.fillMaxWidth()
							.height(10.dp)
					)
					LiquidButton(
						onClick = { showToast(scope, cToastState, context, "2") },
						backdrop = liquidDrop,
						surfaceColor = Color.Green.copy(0.2f)
					) { Text("Liquid Button 2") }
					Spacer(
						Modifier
							.fillMaxWidth()
							.height(10.dp)
					)
					LiquidButton(
						onClick = { showToast(scope, cToastState, context, "3") },
						backdrop = liquidDrop,
						tint = Color(0xFF0088FF)
					) { Text("Liquid Button 3") }
					Spacer(
						Modifier
							.fillMaxWidth()
							.height(10.dp)
					)

					LiquidToggle(
						selected = { selected },
						onSelect = { selected = it },
						backdrop = liquidDrop,
					)
					Spacer(
						Modifier
							.fillMaxWidth()
							.height(10.dp)
					)

					LiquidToggle(
						selected = { selected },
						onSelect = { selected = it },
						backdrop = rememberCanvasBackdrop { drawRect(backgroundColor) },
					)
					Spacer(
						Modifier
							.fillMaxWidth()
							.height(10.dp)
					)
					LiquidSlider(
						value = { value },
						onValueChange = { value = it },
						valueRange = 0f..100f,
						visibilityThreshold = 0.01f,
						backdrop = liquidDrop,
					)
					Spacer(
						Modifier
							.fillMaxWidth()
							.height(10.dp)
					)
					LiquidSlider(
						value = { value },
						onValueChange = { value = it },
						valueRange = 0f..100f,
						visibilityThreshold = 0.01f,
						backdrop = rememberCanvasBackdrop { drawRect(backgroundColor) },
					)
				}
			}

			if (selected) {
				showToast(scope, cToastState, context, "Selected")
			}

			CToastHost(
				hostState = cToastState,
				modifier = Modifier
					.fillMaxSize()
					.padding(horizontal = 30.dp)
			)
		}
	}
}

private fun showToast(
	scope: CoroutineScope,
	cToastState: CToastState,
	context: Context,
	message: String
) {
	scope.launch {
		cToastState.setAndShow(
			title = context.getString(R.string.app_name),
			message = "Liquid Button click $message",
			type = CToastType.SUCCESS
		)
	}
}