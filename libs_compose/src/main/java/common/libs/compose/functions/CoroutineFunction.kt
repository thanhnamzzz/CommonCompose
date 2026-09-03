package common.libs.compose.functions

import androidx.annotation.IntRange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.milliseconds

/**
 * Kiểm tra điều kiện định kỳ cho đến khi nó thỏa mãn.
 *
 * @param checkCondition Hàm lambda trả về Boolean: true nếu điều kiện thỏa mãn.
 * @param onConditionMet Hàm lambda sẽ được thực thi khi điều kiện thỏa mãn.
 * @param intervalMs Khoảng thời gian chờ giữa các lần kiểm tra (mặc định 1000ms = 1s).
 * Phải lớn hơn 0, nếu không vòng lặp sẽ quay liên tục và chiếm CPU.
 */
fun CoroutineScope.checkConditionLoop(
	checkCondition: () -> Boolean,
	onConditionMet: () -> Unit,
	@IntRange(from = 1) intervalMs: Long = 1000L
) {
	require(intervalMs > 0) { "intervalMs must be positive" }
	launch(Dispatchers.IO) {
		while (!checkCondition()) {
			delay(intervalMs.milliseconds)
		}
		withContext(Dispatchers.Main) {
			onConditionMet()
		}
	}
}

/**
 * Phiên bản của [checkConditionLoop] trả về Job để có thể hủy thủ công.
 */
fun CoroutineScope.checkConditionLoopJob(
	checkCondition: () -> Boolean,
	onConditionMet: () -> Unit,
	@IntRange(from = 1) intervalMs: Long = 1000L
): Job {
	require(intervalMs > 0) { "intervalMs must be positive" }
	return launch(Dispatchers.IO) {
		while (isActive && !checkCondition()) {
			delay(intervalMs.milliseconds)
		}
		if (isActive) {
			withContext(Dispatchers.Main) {
				onConditionMet()
			}
		}
	}
}

/**
 * Kiểm tra điều kiện định kỳ trong một khoảng thời gian tối đa.
 * @param checkCondition Hàm lambda trả về Boolean: true nếu điều kiện thỏa mãn.
 * @param onConditionMet Hàm lambda sẽ được thực thi sau khi vòng lặp dừng (dù là do đạt điều kiện hay hết giờ).
 * @param timeoutMs Thời gian tối đa cho phép kiểm tra (tính bằng mili giây). Không được là số âm.
 * @param intervalMs Khoảng thời gian chờ giữa các lần kiểm tra (mặc định 1000ms = 1s).
 * Phải lớn hơn 0, nếu không vòng lặp sẽ quay liên tục và chiếm CPU.
 */
fun CoroutineScope.checkConditionLoopWithTimeout(
	checkCondition: () -> Boolean,
	onConditionMet: (Boolean) -> Unit,
	@IntRange(from = 0) timeoutMs: Long,
	@IntRange(from = 1) intervalMs: Long = 1000L
) {
	require(timeoutMs >= 0) { "timeoutMs must not be negative" }
	require(intervalMs > 0) { "intervalMs must be positive" }
	launch(Dispatchers.IO) {
		val result: Boolean = withTimeoutOrNull(timeoutMs.milliseconds) {
			while (!checkCondition()) {
				delay(intervalMs.milliseconds)
			}
			return@withTimeoutOrNull true
		} ?: false

		withContext(Dispatchers.Main) {
			onConditionMet(result)
		}
	}
}

/**
 * Phiên bản của [checkConditionLoopWithTimeout] trả về Job để có thể hủy thủ công.
 */
fun CoroutineScope.checkConditionLoopWithTimeoutJob(
	checkCondition: () -> Boolean,
	onConditionMet: (Boolean) -> Unit,
	@IntRange(from = 0) timeoutMs: Long,
	@IntRange(from = 1) intervalMs: Long = 1000L
): Job {
	require(timeoutMs >= 0) { "timeoutMs must not be negative" }
	require(intervalMs > 0) { "intervalMs must be positive" }
	return launch(Dispatchers.IO) {
		val result: Boolean = withTimeoutOrNull(timeoutMs.milliseconds) {
			while (isActive && !checkCondition()) {
				delay(intervalMs.milliseconds)
			}
			// Chỉ tới được đây khi vòng lặp kết thúc bình thường (điều kiện đã thỏa mãn).
			// Nếu hết giờ hoặc bị hủy, delay() sẽ ném CancellationException
			// và withTimeoutOrNull trả về null -> result = false.
			true
		} ?: false

		if (isActive) {
			withContext(Dispatchers.Main) {
				onConditionMet(result)
			}
		}
	}
}