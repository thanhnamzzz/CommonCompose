package common.commons_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import common.commons_compose.ui.theme.CommonComposeTheme
import common.libs.compose.extensions.SetNavigationBarContentColor

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			CommonComposeTheme {
				window.SetNavigationBarContentColor(Color.Transparent)
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//					NavigationType1(innerPadding)
					NavigationType2(innerPadding)
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
	openColorPicker: () -> Unit,
	openImagePicker: () -> Unit,
	openAnimationView: () -> Unit,
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
		Button(onClick = { openColorPicker() }) { Text("Color picker") }
		Button(onClick = { openImagePicker() }) { Text("Image picker") }
		Button(onClick = { openAnimationView() }) { Text("Animation View") }
	}
}