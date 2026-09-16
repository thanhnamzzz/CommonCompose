package common.libs.compose.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HandlerFunction(timeWait: Long, callback: () -> Unit) {
	val lifecycleOwner = LocalLifecycleOwner.current
	LaunchedEffect(timeWait) {
		delay(timeWait.milliseconds)
		if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
			callback()
		}
	}
}

@Composable
fun HandlerComposable(
	timeWait: Long, content: @Composable () -> Unit
) {
	val lifecycleOwner = LocalLifecycleOwner.current
	var show by remember { mutableStateOf(false) }

	LaunchedEffect(timeWait) {
		delay(timeWait.milliseconds)
		if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
			show = true
		}
	}

	if (show) {
		content()
	}
}