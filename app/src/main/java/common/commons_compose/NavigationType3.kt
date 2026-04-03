package common.commons_compose

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import common.commons_compose.animation.AnimationView
import common.commons_compose.colorPicker.ColorPicker
import common.commons_compose.colorPicker.ImagePicker
import common.commons_compose.liquidGlass.LiquidLazyColumn
import common.commons_compose.liquidGlass.LiquidView
import common.libs.compose.stackManager.rememberMultiStackNavManager

@Composable
fun Context.NavigationType3(
	innerPadding: PaddingValues,
	openBottomNavigationBar: () -> Unit
) {
//	val navManager = remember { MultiStackNavManager<Screen>(Screen.Home, false) }
	val navManager = rememberMultiStackNavManager<Screen>(Screen.Home, false)
	Column(
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		//NavDisplay cần có androidx.navigation3:navigation3 (hoặc bản navigation3-ui)
		NavDisplay(
			backStack = navManager.backStack,
			onBack = { navManager.pop() },
			entryDecorators = listOf(
				//Tương tự như NavDisplay
				rememberSaveableStateHolderNavEntryDecorator(),
				//Cần có androidx.lifecycle:lifecycle-viewmodel-navigation3
				rememberViewModelStoreNavEntryDecorator()
			),
			entryProvider = { key ->
				when (key) {
					is Screen.Home -> {
						NavEntry(key) {
							HomeScreen(
								modifier = Modifier.padding(innerPadding),
								openDialog = { navManager.push(Screen.Dialog) },
								openCropImage = { navManager.push(Screen.CropImage) },
								openToast = { navManager.push(Screen.Toast) },
								openLiquidGlass = { navManager.push(Screen.LiquidGlass) },
								openColorPicker = { navManager.push(Screen.ColorPicker) },
								openImagePicker = { navManager.push(Screen.ImagePicker) },
								openAnimationView = { navManager.push(Screen.AnimationView) },
								openCountDownTimer = { navManager.push(Screen.CountDownTimer) },
								openBottomNavigationBar = openBottomNavigationBar
							)
						}
					}

					is Screen.Dialog -> {
						NavEntry(key) { DialogScreen(Modifier.padding(innerPadding)) }
					}

					is Screen.Toast -> {
						NavEntry(key) {
							ToastScreen(
								Modifier
									.fillMaxSize()
									.padding(innerPadding),
								this@NavigationType3
							)
						}
					}

					is Screen.CropImage -> {
						NavEntry(key) {
							ContentCropImage(
								Modifier
									.fillMaxSize()
									.padding(innerPadding)
							)
						}
					}

					is Screen.LiquidGlass -> {
						NavEntry(key) {
							LiquidView(
								modifier = Modifier.fillMaxSize(),
								innerPadding = innerPadding,
								context = this@NavigationType3,
								openLazyColumn = {
									navManager.push(Screen.LazyColumnLiquid)
								}
							)
						}
					}

					is Screen.ColorPicker -> {
						NavEntry(key) { ColorPicker() }
					}

					is Screen.ImagePicker -> {
						NavEntry(key) { ImagePicker(Modifier.padding(innerPadding)) }
					}

					is Screen.AnimationView -> {
						NavEntry(key) { AnimationView(modifier = Modifier.padding(innerPadding)) }
					}

					is Screen.LazyColumnLiquid -> {
						NavEntry(key) { LiquidLazyColumn() }
					}

					is Screen.CountDownTimer -> {
						NavEntry(key) {
							TestCountDownTimerCompose(
								modifier = Modifier.padding(
									innerPadding
								)
							)
						}
					}
				}
			}
		)
	}
}