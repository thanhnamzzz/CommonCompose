package common.commons_compose

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import common.commons_compose.colorPicker.ColorPicker
import common.commons_compose.colorPicker.ImagePicker
import common.commons_compose.liquidGlass.LiquidView

@Composable
fun Context.NavigationType1(
	innerPadding: PaddingValues,
	openBottomNavigationBar: () -> Unit
) {
	val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }
	Column(
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		NavDisplay(
			modifier = Modifier.padding(innerPadding),
			backStack = backStack,
			onBack = {
				backStack.removeLastOrNull()
			},
			entryProvider = entryProvider {
				entry<Screen.Home> {
					HomeScreen(
						openDialog = { backStack.add(Screen.Dialog) },
						openCropImage = { backStack.add(Screen.CropImage) },
						openToast = { backStack.add(Screen.Toast) },
						openLiquidGlass = { backStack.add(Screen.LiquidGlass) },
						openColorPicker = { backStack.add(Screen.ColorPicker) },
						openImagePicker = { backStack.add(Screen.ImagePicker) },
						openAnimationView = { backStack.add(Screen.AnimationView) },
						openCountDownTimer = { backStack.add(Screen.CountDownTimer) },
						openBottomNavigationBar = openBottomNavigationBar
					)
				}
				entry<Screen.Dialog> { DialogScreen() }
				entry<Screen.Toast> {
					ToastScreen(Modifier.fillMaxSize(), this@NavigationType1)
				}
				entry<Screen.CropImage> { ContentCropImage(Modifier.fillMaxSize()) }
				entry<Screen.LiquidGlass> {
					LiquidView(
						modifier = Modifier.fillMaxSize(),
						innerPadding = innerPadding,
						context = this@NavigationType1,
						openLazyColumn = {
							backStack.add(Screen.LazyColumnLiquid)
						}
					)
				}
				entry<Screen.ColorPicker> { ColorPicker() }
				entry<Screen.ImagePicker> { ImagePicker() }
			}
		)
	}
}