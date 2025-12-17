package common.libs.compose.extensions

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun handlerFunction(timeWait: Long, callback: () -> Unit) {
	Handler(Looper.getMainLooper()).postDelayed({ callback() }, timeWait)
}

fun LifecycleOwner.handlerFunction(
	timeWait: Long,
	callback: () -> Unit
) {
	lifecycleScope.launch {
		delay(timeWait)
		if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
			callback()
		}
	}
}

@Composable
fun HandlerFunction(timeWait: Long, callback: () -> Unit) {
	val lifecycleOwner = LocalLifecycleOwner.current
	LaunchedEffect(timeWait) {
		delay(timeWait)
		if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
			callback()
		}
	}
}