package common.libs.compose.functions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

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
    private val totalMillis: Long,
    private val intervalMillis: Long = 1000L,
    private val onTick: (Long) -> Unit = {},
    private val onFinish: () -> Unit = {}
) {
    private var job: Job? = null
    private var isPaused = false

    private val _time = MutableStateFlow(totalMillis)
    val time: StateFlow<Long> = _time

    fun start(scope: CoroutineScope) {
        if (job != null) return

        if (_time.value <= 0) _time.update { totalMillis }
        job = scope.launch {
            onTick(_time.value)
            while (_time.value > 0 && isActive) {
                if (!isPaused) {
                    delay(intervalMillis.milliseconds)
                    _time.update { it - intervalMillis }
                    onTick(_time.value)
                } else {
                    delay(100.milliseconds)
                }
            }

            if (_time.value <= 0 && isActive) {
                finishImmediately()
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
        _time.update { 0L }
        onFinish()
        cancel()
    }

    fun cancel() {
        _time.update { 0L }
        job?.cancel()
        job = null
    }
}