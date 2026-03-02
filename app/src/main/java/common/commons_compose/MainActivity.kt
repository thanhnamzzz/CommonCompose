package common.commons_compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import common.commons_compose.ui.theme.CommonComposeTheme
import common.libs.compose.extensions.SetNavigationBarContentColor
import common.libs.compose.functions.rememberComposeTimer

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
    openCountDownTimer: () -> Unit
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
        Button(onClick = { openCountDownTimer() }) { Text("CountDownTimer Compose") }
    }
}

@Composable
fun TestCountDownTimerCompose(
    modifier: Modifier = Modifier
) {
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