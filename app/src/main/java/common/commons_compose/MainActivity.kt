package common.commons_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import common.commons_compose.liquidGlass.LiquidView
import common.commons_compose.ui.theme.CommonComposeTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			CommonComposeTheme {
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
									)
								}
								entry<Screen.Dialog> { DialogScreen() }
								entry<Screen.Toast> {
									ToastScreen(Modifier.fillMaxSize(), this@MainActivity)
								}
								entry<Screen.CropImage> { ContentCropImage(Modifier.fillMaxSize()) }
								entry<Screen.LiquidGlass> {
									LiquidView(
										Modifier.fillMaxSize(),
										this@MainActivity
									)
								}
							}
						)
					}
				}
			}
		}
	}
}

@Composable
fun HomeScreen(
	modifier: Modifier = Modifier,
	openDialog: () -> Unit,
	openCropImage: () -> Unit,
	openToast: () -> Unit,
	openLiquidGlass: () -> Unit,
) {
	val scrollState = rememberScrollState()
	Column(
		modifier = modifier
			.fillMaxSize()
			.verticalScroll(scrollState),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Button(onClick = { openDialog() }) { Text("Test Dialog") }
		Button(onClick = { openCropImage() }) { Text("Crop Image") }
		Button(onClick = { openToast() }) { Text("Test CToast") }
		Button(onClick = { openLiquidGlass() }) { Text("Liquid Glass") }
	}
}