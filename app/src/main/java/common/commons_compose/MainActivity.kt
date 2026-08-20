package common.commons_compose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import common.commons_compose.bottomNavigationBar.BottomNavigationBar
import common.commons_compose.ui.theme.CommonComposeTheme
import common.commons_compose.viewModels.CountDownViewModel
import common.commons_compose.viewModels.HomeViewModel
import common.libs.compose.extensions.SetNavigationBarContentColor
import common.libs.compose.functions.rememberComposeTimer
import common.libs.compose.shimmer.LocalShimmerTheme
import common.libs.compose.shimmer.defaultShimmerTheme
import common.libs.compose.shimmer.shimmer
import common.libs.compose.shimmer.shimmerOverlay
import common.libs.compose.shimmer.shimmerSpec
import common.libs.compose.toast.CToastFillComponent
import common.libs.compose.toast.CToastGradientComponent
import common.libs.compose.toast.CToastOutlinedComponent
import common.libs.compose.toast.CToastStackedComponent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			CommonComposeTheme {
				window.SetNavigationBarContentColor(Color.Transparent)
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//					NavigationType1(innerPadding)
//					NavigationType2(
//						innerPadding = innerPadding,
//						openBottomNavigationBar = { openBottomNavigationBar() }
//					)
					NavigationType3(
						innerPadding = innerPadding,
						openBottomNavigationBar = { openBottomNavigationBar() }
					)
				}
			}
		}
	}

	private fun openBottomNavigationBar() {
		startActivity(Intent(this, BottomNavigationBar::class.java))
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
	openCountDownTimer: () -> Unit,
	openBottomNavigationBar: () -> Unit,
	openHazeSample: () -> Unit,
	viewModel: HomeViewModel = hiltViewModel()
) {
	val scrollState = rememberScrollState()
	LaunchedEffect(Unit) {
		viewModel.testFunction()
	}
	CompositionLocalProvider(
		LocalShimmerTheme provides creditCardTheme,
	) {
		Column(
			modifier = modifier
				.fillMaxSize()
				.background(Color.LightGray)
				.verticalScroll(scrollState),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Button(onClick = { openDialog() }) { Text("Test Dialog") }
			Button(onClick = { openCropImage() }) { Text("Crop Image") }
			Button(onClick = { openToast() }) { Text("Test CToast") }
			Button(onClick = { openLiquidGlass() }) { Text("Liquid Glass") }
			Button(onClick = { openColorPicker() }) { Text("Color picker") }
			Button(onClick = { openImagePicker() }) { Text("Image picker") }
			Button(onClick = { openHazeSample() }) { Text("Haze Blur Sample") }
			Button(
				modifier = Modifier
					.clip(RoundedCornerShape(40.dp))
					.shimmerOverlay(
						duration = 1300,
						rotation = 40f,
						shimmerWidth = 70.dp,
						shaderColors = listOf(
							Color.White.copy(alpha = 0f),
							Color.White.copy(alpha = 0.7f),
							Color.White.copy(alpha = 0f),
						),
						blendMode = BlendMode.Hardlight
					),
				onClick = { openAnimationView() }
			) { Text("Animation View") }
			Button(
				modifier = Modifier
					.clip(RoundedCornerShape(20.dp))
					.shimmer(),
				onClick = { openCountDownTimer() }) { Text("CountDownTimer Compose") }

			Box(
				modifier = Modifier
					.clickable(
						enabled = true,
						onClick = openBottomNavigationBar
					)
					.size(width = 200.dp, height = 100.dp)
					.background(Color.Blue, shape = RoundedCornerShape(40.dp))
					.clip(RoundedCornerShape(40.dp))
					.shimmerOverlay(
						duration = 1500,
						rotation = 20f,
						shimmerWidth = 220.dp,
						shaderColors = listOf(
							Color.White.copy(alpha = 0f),
//                            Color.White.copy(alpha = 0.6f),
							Color.White.copy(alpha = 0.8f),
//                            Color.White.copy(alpha = 0.6f),
							Color.White.copy(alpha = 0f),
						),
						blendMode = BlendMode.Hardlight
					)
			) {
				Text(
					modifier = Modifier
						.fillMaxWidth()
						.align(Alignment.Center),
					text = "Open Bottom Navigation Bar",
					textAlign = TextAlign.Center,
					fontSize = 14.sp,
					color = Color.White
				)
			}
		}
	}
}

@Composable
fun TestCountDownTimerCompose(
	modifier: Modifier = Modifier,
	viewModel: CountDownViewModel = hiltViewModel()
) {
	LaunchedEffect(Unit) {
		viewModel.testFunction()
	}
	val timer = rememberComposeTimer(
		totalMillis = 10000L,
		onFinish = {
			Log.i("Namzzz", "TestCountDownTimerCompose: onFinish")
		}
	)

	val currentTime = timer.time.collectAsState()

	val lifecycleOwner = LocalLifecycleOwner.current
	val scope = rememberCoroutineScope()

	DisposableEffect(lifecycleOwner) {
		val observer = LifecycleEventObserver { _, event ->
			when (event) {
				Lifecycle.Event.ON_RESUME -> {
					Log.d("Namzzz", "TestCountDownTimerCompose: lifecycle onResume")
					timer.resume()
				}

				Lifecycle.Event.ON_PAUSE -> {
					Log.d("Namzzz", "TestCountDownTimerCompose: lifecycle onPause")
					timer.pause()
				}

				else -> {}
			}
		}
		lifecycleOwner.lifecycle.addObserver(observer)
		onDispose {
			Log.d("Namzzz", "TestCountDownTimerCompose: onDispose")
			timer.cancel()
			lifecycleOwner.lifecycle.removeObserver(observer)
		}
	}

	Column(
		modifier = modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(text = currentTime.value.toString())
		Button(onClick = { timer.start(scope) }) { Text("Start") }
		Button(onClick = { timer.pause() }) { Text("Pause") }
		Button(onClick = { timer.resume() }) { Text("Resume") }
		Button(onClick = { timer.finishImmediately() }) { Text("Finish") }
		Button(onClick = { timer.cancel() }) { Text("Cancel") }
	}
}


private val creditCardTheme = defaultShimmerTheme.copy(
	animationSpec = infiniteRepeatable(
		animation = shimmerSpec(
			durationMillis = 2000,
			delayMillis = 500,
			easing = LinearEasing,
		),
	),
	blendMode = BlendMode.Hardlight,
	rotation = 25f,
	shaderColors = listOf(
		Color.White.copy(alpha = 0.0f),
		Color.White.copy(alpha = 0.7f),
		Color.White.copy(alpha = 0.0f),
	),
	shaderColorStops = null,
	shimmerWidth = 50.dp,
)

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun ToastFillStylesPreview() {
	Column(
		modifier = Modifier
			.padding(16.dp)
			.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		Text(
			text = "Style 1: Pill (Current)",
			style = MaterialTheme.typography.labelLarge,
			color = Color.Black
		)
		CToastFillComponent(
			title = "Success",
			message = "Your action was completed successfully.",
			iconRes = common.libs.compose.R.drawable.baseline_check_circle_24,
			iconColor = Color(0xFF10B981),
			backgroundColor = Color(0xFF10B981).copy(alpha = 0.1f),
			textColor = Color.Black.copy(alpha = 0.8f),
			iconScale = 1f
		)

		Text(
			text = "Style 2: Modern Outlined",
			style = MaterialTheme.typography.labelLarge,
			color = Color.Black
		)
		CToastOutlinedComponent(
			title = "Information",
			message = "This is a modern outlined toast style.",
			iconRes = common.libs.compose.R.drawable.outline_info_24,
			iconColor = Color(0xFF3B82F6),
			textColor = Color.Black.copy(alpha = 0.7f),
			iconScale = 1f
		)

		Text(
			text = "Style 3: Gradient Impact",
			style = MaterialTheme.typography.labelLarge,
			color = Color.Black
		)
		CToastGradientComponent(
			title = "Attention",
			message = "Important update available for your account.",
			iconRes = common.libs.compose.R.drawable.outline_warning_24,
			iconColor = Color(0xFFF59E0B),
			backgroundColor = Color(0xFFFFFBEB),
			iconScale = 1f
		)

		Text(
			text = "Style 4: Stacked Vertical",
			style = MaterialTheme.typography.labelLarge,
			color = Color.Black
		)
		CToastStackedComponent(
			title = "Event Reminder",
			message = "You have a meeting starting in 15 minutes in Room 302.",
			iconRes = common.libs.compose.R.drawable.outline_info_24,
			iconColor = Color(0xFF8B5CF6),
			backgroundColor = Color(0xFFF5F3FF),
			textColor = Color.Black.copy(alpha = 0.8f),
			iconScale = 1f
		)

//		Text(
//			text = "Style 5: Icon Right",
//			style = MaterialTheme.typography.labelLarge,
//			color = Color.Black
//		)
//		CToastIconRightComponent(
//			title = "Download Complete",
//			message = "Your file has been downloaded successfully to the folder.",
//			iconRes = common.libs.compose.R.drawable.baseline_check_circle_24,
//			iconColor = Color(0xFF10B981),
//			backgroundColor = Color(0xFFECFDF5),
//			textColor = Color.Black.copy(alpha = 0.8f),
//			iconScale = 1f
//		)
	}
}