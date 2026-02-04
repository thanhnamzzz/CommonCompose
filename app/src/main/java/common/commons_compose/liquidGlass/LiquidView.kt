package common.commons_compose.liquidGlass

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import common.commons_compose.R
import common.libs.compose.extensions.contrastingColor
import common.libs.compose.liquidGlass.LiquidContainer
import common.libs.compose.liquidGlass.components.LiquidBottomTab
import common.libs.compose.liquidGlass.components.LiquidBottomTabs
import common.libs.compose.liquidGlass.components.LiquidButton
import common.libs.compose.liquidGlass.components.LiquidSlider
import common.libs.compose.liquidGlass.components.LiquidToggle
import common.libs.compose.liquidGlass.components.rememberRectBackdrop
import common.libs.compose.toast.CToastConfiguration
import common.libs.compose.toast.CToastHost
import common.libs.compose.toast.CToastState
import common.libs.compose.toast.CToastType
import common.libs.compose.toast.DURATION_SHORT
import common.libs.compose.toast.LocalCToastConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LiquidView(
	modifier: Modifier = Modifier,
	innerPadding: PaddingValues,
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

	LiquidContainer(
		modifier = modifier.fillMaxSize(),
		background = R.mipmap.wallpaper_light
	) { liquidDrop ->
		CompositionLocalProvider(LocalCToastConfig provides CToastConfiguration()) {
			Box {
				Column(
					modifier = modifier
						.fillMaxSize()
						.padding(innerPadding),
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
						backdrop = liquidDrop,
						shape = RoundedCornerShape(0.dp),
						buttonHeight = 90.dp,
						paddingHorizontal = 60.dp
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
					Spacer(Modifier.size(5.dp))
					LiquidToggle(
						selected = { selected },
						onSelect = { selected = it },
						backdrop = liquidDrop,
//						accentColor = Color(0xFFFFFFFF),
						thumbColor = Color(0xFFFFFFFF),
//						thumbColorShadow = Color(0xFFF4511E).contrastingColor(),
//						paddingTrack = 5.dp,
						thumbWidth = 30.dp,
						dragDistance = 30.dp,
//						scalePressed = 2f
					)
					Spacer(
						Modifier
							.fillMaxWidth()
							.height(10.dp)
					)

					LiquidToggle(
						selected = { selected },
						onSelect = { selected = it },
						backdrop = rememberRectBackdrop { drawRect(backgroundColor) },
//						backdrop = rememberCanvasBackdrop { drawRect(backgroundColor) },
					)
					Spacer(
						Modifier
							.fillMaxWidth()
							.height(10.dp)
					)
					LiquidSlider(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 20.dp),
						value = { value },
						onValueChange = { value = it },
						valueRange = 0f..100f,
						backdrop = liquidDrop,
						trackHeight = 10.dp,
						accentColor = Color(0xFFFF2222),
						shapeThumb = RoundedCornerShape(10.dp),
						thumbColor = Color(0xFF9C27B0),
						thumbColorShadow = Color(0xFF9C27B0).contrastingColor(),
						scalePressed = 1.2f
					)
					Spacer(
						Modifier
							.fillMaxWidth()
							.height(10.dp)
					)
					LiquidSlider(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 20.dp),
						value = { value },
						onValueChange = { value = it },
						valueRange = 0f..100f,
						backdrop = rememberRectBackdrop { drawRect(backgroundColor) },
//						backdrop = rememberCanvasBackdrop { drawRect(backgroundColor) },
					)
					Spacer(
						Modifier
							.fillMaxWidth()
							.height(10.dp)
					)

					BottomTab(
						modifier = Modifier.fillMaxWidth(),
						liquidDrop = liquidDrop,
						onTabSelected = {
							showToast(scope, cToastState, context, "Selected Tab $it", 800)
						},
						shape = RoundedCornerShape(20.dp)
					)

					BottomTab(
						modifier = Modifier.fillMaxWidth(),
						liquidDrop = liquidDrop,
						onTabSelected = {
							showToast(scope, cToastState, context, "Selected Tab $it", 800)
						},
						shape = RoundedCornerShape(40.dp),
						addItem = true
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
					.padding(horizontal = 35.dp, vertical = 50.dp)
			)
		}
	}
}

@Composable
fun BottomTab(
	modifier: Modifier = Modifier,
	liquidDrop: Backdrop,
	shape: RoundedCornerShape = RoundedCornerShape(50.dp),
	addItem: Boolean = false,
	onTabSelected: (index: Int) -> Unit,
) {
	var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
	LiquidBottomTabs(
		selectedTabIndex = { selectedTabIndex },
		onTabSelected = {
			selectedTabIndex = it
			onTabSelected(selectedTabIndex)
		},
		backdrop = liquidDrop,
		tabsCount = if (addItem) 4 else 3,
		containerColor = Color(0xFFFAFAFA).copy(0.1f),
		accentColor = Color(0xFFFF0000),
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 15.dp, vertical = 5.dp),
		bottomTabHeight = 80.dp,
		selectedTabHeight = 65.dp,
		shape = shape,
		paddingValues = 10.dp,
		tabPadding = 20.dp,
//		scalePressed = 1.8f
	) {
		LiquidBottomTab(onClick = { selectedTabIndex = 0 }) {
			BottomTab(
				idIcon = R.drawable.icon_anchor,
				title = "Anchor",
				titleSize = 14.sp
			)
		}
		LiquidBottomTab(onClick = { selectedTabIndex = 1 }) {
			BottomTab(
				idIcon = R.drawable.icon_reader,
				title = "Reader",
				titleSize = 14.sp
			)
		}
		LiquidBottomTab(onClick = { selectedTabIndex = 2 }) {
			BottomTab(
				idIcon = R.drawable.icon_color_lens,
				title = "Color Lens",
				titleSize = 14.sp
			)
		}
		if (addItem) {
			LiquidBottomTab(onClick = { selectedTabIndex = 3 }) {
				BottomTab(
					idIcon = R.drawable.icon_anchor,
					title = "Anchor",
					titleSize = 14.sp
				)
			}
		}
	}
}

@Composable
fun BottomTab(
	idIcon: Int,
	title: String = "",
	titleSize: TextUnit = 14.sp
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Spacer(modifier = Modifier.height(5.dp))
		Image(
			painter = painterResource(idIcon),
			contentDescription = null
		)
		Text(
			text = title,
			fontSize = titleSize
		)
		Spacer(modifier = Modifier.height(5.dp))
	}
}

private fun showToast(
	scope: CoroutineScope,
	cToastState: CToastState,
	context: Context,
	message: String,
	duration: Long = DURATION_SHORT
) {
	scope.launch {
		cToastState.setAndShow(
			title = context.getString(R.string.app_name),
			message = "Liquid Button click $message",
			type = CToastType.SUCCESS,
			duration = duration
		)
	}
}