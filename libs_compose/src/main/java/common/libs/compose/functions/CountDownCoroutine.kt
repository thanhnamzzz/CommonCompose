package common.libs.compose.functions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun rememberComposeTimer(
	totalMillis: Long,
	intervalMillis: Long = 1000L,
	onTick: (Long) -> Unit = {},
	onFinish: () -> Unit = {}
): CountDownTimerState {
	return remember {
		CountDownTimerState(
			totalMillis = totalMillis,
			intervalMillis = intervalMillis,
			onTick = onTick,
			onFinish = onFinish
		)
	}
}

class CountDownTimerState(
	totalMillis: Long,
	private val intervalMillis: Long = 1000L,
	private val onTick: (Long) -> Unit = {},
	private val onFinish: () -> Unit = {}
) {
	private var job: Job? = null
	private var isPaused = false

	private val _timeLeft = mutableLongStateOf(totalMillis)
	val timeLeft: State<Long> = _timeLeft

	fun start(scope: CoroutineScope) {
		if (job != null) return

		job = scope.launch {
			onTick(_timeLeft.longValue)
			while (_timeLeft.longValue > 0 && isActive) {
				if (!isPaused) {
					delay(intervalMillis)
					_timeLeft.longValue -= intervalMillis
					onTick(_timeLeft.longValue)
				} else {
					delay(100)
				}
			}

			if (_timeLeft.longValue <= 0 && isActive) {
				_timeLeft.longValue = 0
				onFinish()
			}
		}
	}

	fun pause() {
		isPaused = true
	}

	fun resume() {
		isPaused = false
	}

	fun finishImmediately() {
		_timeLeft.longValue = 0
		onFinish()
		cancel()
	}

	fun cancel() {
		_timeLeft.longValue = 0
		job?.cancel()
		job = null
	}
}