/** Clone from https://github.com/dononcharles/NiceToast
 * from commit 91b458d
 */
package common.libs.compose.toast

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID


/**
 * Một data class lưu trữ tất cả cấu hình và nội dung cho một thông báo toast đơn lẻ.
 * Đối tượng này được thiết kế để không thể thay đổi (immutable).
 *
 * @property id Một mã định danh duy nhất cho phiên bản toast cụ thể này. Điều này rất quan trọng đối với
 * LaunchedEffect của Jetpack Compose để kích hoạt và kích hoạt lại chính xác các hiệu ứng động và
 * bộ hẹn giờ, ngay cả khi nội dung thông báo toast giống hệt với nội dung trước đó.
 * @property message Nội dung thông báo chính được hiển thị trong toast.
 * @property title Một tiêu đề tùy chọn được hiển thị phía trên thông báo.
 * @property type Kiểu trực quan của toast (ví dụ: SUCCESS, ERROR).
 * @property duration Thời gian tính bằng mili giây mà toast sẽ hiển thị trước khi
 * nó tự động đóng lại.
 * @property isFullBackground Nếu true, nền toast sẽ là một màu đồng nhất. Nếu false,
 * nó sẽ có kiểu thanh bên (side-bar).
 * @property position Vị trí màn hình nơi toast sẽ xuất hiện (TOP, CENTER, hoặc BOTTOM).
 */
@Stable
internal class CToastData(
	val id: String = UUID.randomUUID().toString(),
	val message: String = "",
	val title: String? = null,
	val type: CToastType = CToastType.SUCCESS,
	val duration: Long = DURATION_SHORT,
	val isFullBackground: Boolean = true,
//	val position: CToastPosition
)


/**
 * Trình quản lý trạng thái cho [CToastHost]. Nó điều khiển hàng đợi và khả năng hiển thị của các toast.
 *
 * Lớp này tương tự như `SnackbarHostState` của Jetpack Compose. Nó nên được tạo
 * và ghi nhớ (remember) bên trong một hàm Composable và được nâng lên (hoisted) để điều khiển việc hiển thị toast.
 * Phương thức `show` là an toàn đa luồng (thread-safe), đảm bảo rằng nhiều cuộc gọi nhanh không làm hỏng trạng thái.
 *
 * @see CToastHost
 */
@Stable
class CToastState {
	/**
	 * Một Mutex để đảm bảo rằng các yêu cầu hiển thị toast được xử lý một cách nguyên tử (atomically). Điều này ngăn chặn
	 * tình trạng tương tranh (race conditions) nếu `show` được gọi từ nhiều coroutine cùng lúc.
	 */
	private val mutex = Mutex()

	/**
	 * Dữ liệu toast hiện đang hiển thị. Đây là một đối tượng trạng thái có thể thay đổi (mutable state) được quan sát bởi
	 * Composable [CToastHost]. Khi giá trị này thay đổi, giao diện người dùng sẽ phản hồi để
	 * hiển thị một toast mới hoặc ẩn toast hiện tại. Hàm setter là riêng tư (private) để đảm bảo tất cả
	 * các thay đổi đều được chuyển qua các phương thức `show` và `dismiss`.
	 */
	internal var currentToastData by mutableStateOf<CToastData?>(null)
		private set

	/**
	 * Hiển thị một toast với các thông số được cung cấp. Hàm này là một hàm tạm dừng (suspending function)
	 * và phải được gọi từ một phạm vi coroutine. Nó đảm bảo rằng chỉ có một toast được hiển thị
	 * tại một thời điểm.
	 *
	 * @param message Nội dung chính của toast.
	 * @param title Một tiêu đề tùy chọn.
	 * @param type Kiểu của toast (SUCCESS, ERROR, v.v.).
	 * @param duration Thời gian toast sẽ hiển thị.
	 * @param isFullBackground Đặt thành true để có màu nền đồng nhất.
	 * @param position Vị trí của toast trên màn hình.
	 */
	suspend fun setAndShow(
		message: String = "",
		title: String? = null,
		type: CToastType = CToastType.SUCCESS,
		duration: Long = DURATION_SHORT,
		isFullBackground: Boolean = true,
//		position: CToastPosition = CToastPosition.BOTTOM
	) {
		// Khóa mutex để đảm bảo rằng chúng ta không có nhiều luồng
		// cố gắng cập nhật currentToastData cùng một lúc.
		mutex.withLock {
			// Tạo một đối tượng dữ liệu mới và gán nó cho trạng thái có thể thay đổi.
			// Việc gán này sẽ kích hoạt quá trình recomposition trong NiceToastHost.
			currentToastData = CToastData(
				message = message,
				title = title,
				type = type,
				duration = duration,
				isFullBackground = isFullBackground,
//				position = position
			)
		}
	}

	suspend fun setAndShow(
		message: String = "",
		title: String? = null,
		type: CToastType = CToastType.SUCCESS,
		duration: Long = DURATION_SHORT,
	) {
		mutex.withLock {
			currentToastData = CToastData(
				message = message,
				title = title,
				type = type,
				duration = duration,
				isFullBackground = true,
			)
		}
	}

	/**
	 * Đóng ngay lập tức toast đang hiển thị, nếu có.
	 * Điều này có thể được sử dụng để đóng thủ công dựa trên hành động của người dùng hoặc logic khác.
	 */
	fun dismiss() {
		currentToastData = null
	}
}