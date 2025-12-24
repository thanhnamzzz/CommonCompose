package common.commons_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import common.commons_compose.ui.theme.CommonComposeTheme
import common.libs.compose.extensions.handlerFunction

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			CommonComposeTheme {
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

					var showDialog1 by remember { mutableStateOf(false) }
					var showDialog2 by remember { mutableStateOf(false) }

					Column (modifier = Modifier.padding(innerPadding)) {
						Greeting(
							name = "Show Dialog 1",
						) {
							showDialog1 = !showDialog1
						}
						Greeting(
							name = "Show Dialog 2",
						) {
							showDialog2 = !showDialog2
						}
					}

					if (showDialog1) {
						ShowDialog(
							onDismissRequest = {
//								handlerFunction(1000) { showDialog = false }
								showDialog1 = false
							},
							onConfirmation = {
//								handlerFunction(1000) { showDialog = false }
								showDialog1 = false
							},
							dialogTitle = "Alert dialog example",
							dialogText = "This is an example of an alert dialog with buttons.",
							icon = Icons.Default.Info
						)
					}

					if (showDialog2) {
						TestDialogFullScreen { showDialog2 = false }
					}
				}
			}
		}
	}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
	Text(
		text = name,
		modifier = modifier
			.clickable(enabled = true, onClick = onClick)
			.padding(vertical = 7.dp, horizontal = 15.dp)
	)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
	CommonComposeTheme {
//		TestDialogCustom(onDismissRequest = {})
//		Greeting("Android")
//		ShowDialog(
//			onDismissRequest = { },
//			onConfirmation = {},
//			dialogTitle = "Alert dialog example",
//			dialogText = "This is an example of an alert dialog with buttons.",
//			icon = Icons.Default.Info
//
//		)

		TestDialogFullScreen {}
	}
}

@Composable
fun ShowDialog(
	onDismissRequest: () -> Unit,
	onConfirmation: () -> Unit,
	dialogTitle: String,
	dialogText: String,
	icon: ImageVector = Icons.Default.Info,
) {
	AlertDialog(
		icon = {
			Icon(icon, contentDescription = "Example Icon")
		},
		title = {
			Text(text = dialogTitle)
		},
		text = {
			Text(text = dialogText)
		},
		onDismissRequest = {
			onDismissRequest()
		},
		confirmButton = {
			TextButton(
				onClick = {
					onConfirmation()
				}
			) {
				Text("Confirm")
			}
		},
		dismissButton = {
			TextButton(
				onClick = {
					onDismissRequest()
				}
			) {
				Text("Dismiss")
			}
		}
	)

}

@Composable
fun TestDialogCustom(onDismissRequest: () -> Unit) {
	val isLightTheme = !isSystemInDarkTheme()
	val contentColor = if (isLightTheme) Color.Black else Color.White
	val containerColor =
		if (isLightTheme) Color(0xFFFAFAFA).copy(0.6f)
		else Color(0xFF121212).copy(0.4f)
	val accentColor =
		if (isLightTheme) Color(0xFF0088FF)
		else Color(0xFF0091FF)

	Dialog(onDismissRequest = {}) {
		Column(
			Modifier
				.padding(40f.dp)
//            .drawBackdrop(
//                backdrop = backdrop,
//                shape = { ContinuousRoundedRectangle(48f.dp) },
//                effects = {
//                    colorControls(
//                        brightness = if (isLightTheme) 0.2f else 0f,
//                        saturation = 1.5f
//                    )
//                    blur(if (isLightTheme) 16f.dp.toPx() else 8f.dp.toPx())
//                    lens(24f.dp.toPx(), 48f.dp.toPx(), depthEffect = true)
//                },
//                highlight = { Highlight.Plain },
//                onDrawSurface = { drawRect(containerColor) }
//            )
				.fillMaxWidth()
				.background(containerColor)
		) {
			BasicText(
				"Dialog Title",
				Modifier.padding(28f.dp, 24f.dp, 28f.dp, 12f.dp),
				style = TextStyle(contentColor, 24f.sp, FontWeight.Medium)
			)

			BasicText(
				LoremIpsum,
				Modifier
					.then(
						if (isLightTheme) {
							// plus darker
							Modifier
						} else {
							// plus lighter
							Modifier.graphicsLayer(blendMode = BlendMode.Plus)
						}
					)
					.padding(24f.dp, 12f.dp, 24f.dp, 12f.dp),
				style = TextStyle(contentColor.copy(0.68f), 15f.sp),
				maxLines = 5
			)

			Row(
				Modifier
					.padding(24f.dp, 12f.dp, 24f.dp, 24f.dp)
					.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(16f.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Row(
					Modifier
						.background(containerColor.copy(0.2f))
						.clickable {}
						.height(48f.dp)
						.weight(1f)
						.padding(horizontal = 16f.dp),
					horizontalArrangement = Arrangement.spacedBy(
						4f.dp,
						Alignment.CenterHorizontally
					),
					verticalAlignment = Alignment.CenterVertically
				) {
					BasicText(
						"Cancel",
						style = TextStyle(contentColor, 16f.sp)
					)
				}
				Row(
					Modifier
						.background(accentColor)
						.clickable {}
						.height(48f.dp)
						.weight(1f)
						.padding(horizontal = 16f.dp),
					horizontalArrangement = Arrangement.spacedBy(
						4f.dp,
						Alignment.CenterHorizontally
					),
					verticalAlignment = Alignment.CenterVertically
				) {
					BasicText(
						"Okay",
						style = TextStyle(Color.White, 16f.sp)
					)
				}
			}
		}
	}
}

@Composable
fun TestDialogFullScreen(
	onDismissRequest: () -> Unit,
) {
	val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_loading_window))
	val progress by animateLottieCompositionAsState(
		composition = composition,
		iterations = LottieConstants.IterateForever
	)
	handlerFunction(5000) { onDismissRequest() }
	Dialog(
		onDismissRequest = {},
		properties = DialogProperties(
			dismissOnBackPress = false,
			dismissOnClickOutside = false,
			usePlatformDefaultWidth = false
		)
	) {
		Card(
			modifier = Modifier
				.fillMaxSize()
				.padding(horizontal = 10.dp),
			shape = RoundedCornerShape(16.dp)
		) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.background(Color.White),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				LottieAnimation(
					modifier = Modifier.height(120.dp),
					composition = composition,
					progress = { progress },
					renderMode = RenderMode.HARDWARE,
				)
				Text(text = "Loading ads...", fontSize = 14.sp, color = Color.Black)
			}
		}
	}
}