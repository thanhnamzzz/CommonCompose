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
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import common.commons_compose.animation.AnimationView
import common.commons_compose.colorPicker.ColorPicker
import common.commons_compose.colorPicker.ImagePicker
import common.commons_compose.liquidGlass.LiquidView
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun Context.NavigationType2(innerPadding: PaddingValues) {
	val backStack = rememberNavBackStack(
		configuration = SavedStateConfiguration {
			serializersModule = SerializersModule {
				polymorphic(NavKey::class) {
					subclass(Screen.Home::class, Screen.Home.serializer())
					subclass(Screen.Dialog::class, Screen.Dialog.serializer())
					subclass(Screen.CropImage::class, Screen.CropImage.serializer())
					subclass(Screen.Toast::class, Screen.Toast.serializer())
					subclass(Screen.LiquidGlass::class, Screen.LiquidGlass.serializer())
					subclass(Screen.ColorPicker::class, Screen.ColorPicker.serializer())
					subclass(Screen.ImagePicker::class, Screen.ImagePicker.serializer())
					subclass(Screen.AnimationView::class, Screen.AnimationView.serializer())
				}
			}
		},
		Screen.Home
	)
	Column(
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		NavDisplay(
//			modifier = Modifier.padding(innerPadding),
			backStack = backStack,
			entryDecorators = listOf(
				rememberSaveableStateHolderNavEntryDecorator(),
				rememberViewModelStoreNavEntryDecorator()
			),
			entryProvider = { key ->
				when (key) {
					is Screen.Home -> {
						NavEntry(key) {
							HomeScreen(
								modifier = Modifier.padding(innerPadding),
								openDialog = { backStack.add(Screen.Dialog) },
								openCropImage = { backStack.add(Screen.CropImage) },
								openToast = { backStack.add(Screen.Toast) },
								openLiquidGlass = { backStack.add(Screen.LiquidGlass) },
								openColorPicker = { backStack.add(Screen.ColorPicker) },
								openImagePicker = { backStack.add(Screen.ImagePicker) },
								openAnimationView = { backStack.add(Screen.AnimationView) }
							)
						}
					}

					is Screen.Dialog -> {
						NavEntry(key) { DialogScreen() }
					}

					is Screen.Toast -> {
						NavEntry(key) { ToastScreen(Modifier.fillMaxSize(), this@NavigationType2) }
					}

					is Screen.CropImage -> {
						NavEntry(key) { ContentCropImage(Modifier.fillMaxSize()) }
					}

					is Screen.LiquidGlass -> {
						NavEntry(key) {
							LiquidView(
								modifier = Modifier.fillMaxSize(),
								innerPadding = innerPadding,
								context = this@NavigationType2
							)
						}
					}

					is Screen.ColorPicker -> {
						NavEntry(key) { ColorPicker() }
					}

					is Screen.ImagePicker -> {
						NavEntry(key) { ImagePicker() }
					}

					is Screen.AnimationView -> {
						NavEntry(key) { AnimationView() }
					}

					else -> error("Unknown key: $key")
				}
			}
		)
	}
}