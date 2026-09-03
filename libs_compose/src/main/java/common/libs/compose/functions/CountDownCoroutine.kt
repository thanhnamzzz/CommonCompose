package common.libs.compose.functions

import androidx.annotation.IntRange
import androidx.annotation.MainThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration.Companion.milliseconds

/**
 * Tạo và ghi nhớ một [CountDownTimerState] gắn với composition hiện tại.
 *
 * - Timer được tạo lại khi [totalMillis] hoặc [intervalMillis] thay đổi, và instance cũ
 *   sẽ bị hủy để không rò coroutine.
 * - [onTick] và [onFinish] luôn trỏ tới lambda mới nhất của lần recomposition gần nhất,
 *   nên callback không bị "đông cứng" ở giá trị state của lần compose đầu tiên.
 *
 * @param totalMillis Tổng thời gian đếm ngược (ms). Không được là số âm.
 * @param intervalMillis Khoảng thời gian giữa hai lần tick (mặc định 1000ms = 1s). Phải lớn hơn 0.
 * @param onTick Được gọi trên Main thread mỗi lần tick, kèm thời gian còn lại (luôn >= 0).
 * @param onFinish Được gọi trên Main thread đúng một lần cho mỗi lần chạy, khi timer về 0.
 */
@Composable
fun rememberComposeTimer(
    totalMillis: Long,
    @IntRange(from = 1) intervalMillis: Long = 1000L,
    onTick: (Long) -> Unit = {},
    onFinish: () -> Unit = {}
): CountDownTimerState {
    val currentOnTick by rememberUpdatedState(onTick)
    val currentOnFinish by rememberUpdatedState(onFinish)

    val timer = remember(totalMillis, intervalMillis) {
        CountDownTimerState(
            totalMillis = totalMillis,
            intervalMillis = intervalMillis,
            onTick = { remaining -> currentOnTick(remaining) },
            onFinish = { currentOnFinish() }
        )
    }

    DisposableEffect(timer) {
        onDispose { timer.cancel() }
    }

    return timer
}

/**
 * Timer đếm ngược dựa trên coroutine, phát thời gian còn lại qua [time].
 *
 * Các hàm điều khiển thay đổi [job] ([start], [finishImmediately], [cancel], [stop], [reset])
 * được thiết kế để gọi từ Main thread. [pause] và [resume] an toàn với mọi thread.
 *
 * @param totalMillis Tổng thời gian đếm ngược (ms). Không được là số âm.
 * @param intervalMillis Khoảng thời gian giữa hai lần tick (mặc định 1000ms = 1s). Phải lớn hơn 0.
 * @param onTick Gọi trên Main thread mỗi lần tick, kèm thời gian còn lại (luôn >= 0).
 * @param onFinish Gọi trên Main thread đúng một lần cho mỗi lần chạy, khi timer về 0.
 */
class CountDownTimerState(
    private val totalMillis: Long,
    @IntRange(from = 1) private val intervalMillis: Long = 1000L,
    private val onTick: (Long) -> Unit = {},
    private val onFinish: () -> Unit = {}
) {
    init {
        require(totalMillis >= 0) { "totalMillis must not be negative" }
        require(intervalMillis > 0) { "intervalMillis must be positive" }
    }

    private var job: Job? = null

    /** Cờ tạm dừng: dùng StateFlow để vòng lặp chờ thụ động, không phải poll mỗi 100ms. */
    private val _paused = MutableStateFlow(false)

    /** Đảm bảo [onFinish] chỉ chạy đúng một lần cho mỗi lần chạy timer. */
    private val finished = AtomicBoolean(false)

    private val _time = MutableStateFlow(totalMillis)

    /** Thời gian còn lại (ms), luôn nằm trong khoảng [0, totalMillis]. */
    val time: StateFlow<Long> = _time.asStateFlow()

    val isRunning: Boolean get() = job?.isActive == true

    val isPaused: Boolean get() = _paused.value

    /**
     * Bắt đầu đếm ngược trong [scope]. Không làm gì nếu timer đang chạy (kể cả đang pause).
     *
     * Nếu thời gian còn lại đã về 0, timer được nạp lại [totalMillis]; nếu còn dở
     * (sau khi gọi [stop]) thì tiếp tục từ thời gian còn lại. Trạng thái pause được xóa.
     */
    @MainThread
    fun start(scope: CoroutineScope) {
        if (isRunning) return

        if (_time.value <= 0L) _time.value = totalMillis
        _paused.value = false
        finished.set(false)

        job = scope.launch {
            emitTick(_time.value)
            while (isActive && _time.value > 0L) {
                // Chờ tại đây nếu đang pause, không tiêu tốn CPU.
                _paused.first { paused -> !paused }
                delay(intervalMillis.milliseconds)
                // Chặn dưới ở 0 để onTick/time không bao giờ phát ra số âm khi
                // totalMillis không chia hết cho intervalMillis.
                val remaining = (_time.value - intervalMillis).coerceAtLeast(0L)
                _time.value = remaining
                emitTick(remaining)
            }

            if (isActive && _time.value <= 0L) {
                notifyFinished()
            }
        }
    }

    fun pause() {
        _paused.value = true
    }

    fun resume() {
        _paused.value = false
    }

    /**
     * Kết thúc timer ngay lập tức: đưa thời gian về 0, hủy job và gọi [onFinish]
     * (chỉ gọi nếu lần chạy này chưa từng finish).
     */
    @MainThread
    fun finishImmediately() {
        _time.value = 0L
        job?.cancel()
        job = null
        if (finished.compareAndSet(false, true)) {
            onFinish()
        }
    }

    /** Hủy timer và đưa thời gian về 0. Không gọi [onFinish]. */
    @MainThread
    fun cancel() {
        job?.cancel()
        job = null
        _time.value = 0L
    }

    /**
     * Dừng timer nhưng GIỮ thời gian còn lại, để hiển thị/lưu lại hoặc chạy tiếp
     * bằng [start] sau đó. Không gọi [onFinish].
     */
    @MainThread
    fun stop() {
        job?.cancel()
        job = null
    }

    /** Dừng timer và nạp lại [totalMillis], xóa trạng thái pause và cờ finish. */
    @MainThread
    fun reset() {
        stop()
        _paused.value = false
        finished.set(false)
        _time.value = totalMillis
    }

    private suspend fun emitTick(remaining: Long) {
        withContext(Dispatchers.Main.immediate) { onTick(remaining) }
    }

    private suspend fun notifyFinished() {
        _time.value = 0L
        if (finished.compareAndSet(false, true)) {
            withContext(Dispatchers.Main.immediate) { onFinish() }
        }
    }
}