/** Clone from https://github.com/dononcharles/NiceToast
 * from commit 91b458d
 */
package common.libs.compose.toast

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import common.libs.compose.R

/**
 * Cấu hình cho các thành phần NiceToast trong Compose.
 *
 * Lớp này được đánh dấu là @Immutable để giúp trình biên dịch Compose tối ưu hóa quá trình recomposition.
 * Nó tuân theo mẫu giống như builder, nơi bạn tạo một bản sao đã sửa đổi của cấu hình
 * thay vì thay đổi trực tiếp các thuộc tính của nó.
 */
@Immutable
data class CToastConfiguration(
	@param:ColorRes val successToastColor: Int = R.color.success_color,
	@param:ColorRes val errorToastColor: Int = R.color.error_color,
	@param:ColorRes val warningToastColor: Int = R.color.warning_color,
	@param:ColorRes val infoToastColor: Int = R.color.info_color,

	@param:ColorRes val successBackgroundToastColor: Int = R.color.success_bg_color,
	@param:ColorRes val errorBackgroundToastColor: Int = R.color.error_bg_color,
	@param:ColorRes val warningBackgroundToastColor: Int = R.color.warning_bg_color,
	@param:ColorRes val infoBackgroundToastColor: Int = R.color.info_bg_color,
) {
	/**
	 * Hàm nội bộ để phân giải kiểu dáng dựa trên loại toast và chủ đề.
	 * Logic này hiện là một phần của đối tượng cấu hình bất biến.
	 */
	internal fun getStyleSpec(toastType: CToastType/*, darkTheme: Boolean, solidBackground: Boolean*/): StyleSpec {
		val iconColor = when (toastType) {
			CToastType.SUCCESS -> successToastColor
			CToastType.ERROR -> errorToastColor
			CToastType.WARNING -> warningToastColor
			CToastType.INFO -> infoToastColor
		}

		val backgroundColor = when (toastType) {
			CToastType.SUCCESS -> successBackgroundToastColor
			CToastType.ERROR -> errorBackgroundToastColor
			CToastType.WARNING -> warningBackgroundToastColor
			CToastType.INFO -> infoBackgroundToastColor
		}

		val iconRes = when (toastType) {
			CToastType.SUCCESS -> R.drawable.baseline_check_circle_24
			CToastType.ERROR -> R.drawable.outline_error_24
			CToastType.WARNING -> R.drawable.outline_warning_24
			CToastType.INFO -> R.drawable.outline_info_24
		}

		return StyleSpec(
			iconRes = iconRes,
			iconColor = iconColor,
			backgroundColor = backgroundColor,
			defaultTitle = toastType.name.lowercase().replaceFirstChar { it.uppercase() }
		)
	}

	companion object {
		/**
		 * Classic: Các màu tiêu chuẩn của Material Design 2.
		 */
		val Classic = CToastConfiguration()

		/**
		 * Modern (Slate/Professional): Các tông màu Teal và Indigo sạch sẽ, chuyên nghiệp.
		 */
		val Modern = CToastConfiguration(
			successToastColor = R.color.ctoast_success_modern,
			errorToastColor = R.color.ctoast_error_modern,
			warningToastColor = R.color.ctoast_warning_modern,
			infoToastColor = R.color.ctoast_info_modern,
			successBackgroundToastColor = R.color.ctoast_success_bg_modern,
			errorBackgroundToastColor = R.color.ctoast_error_bg_modern,
			warningBackgroundToastColor = R.color.ctoast_warning_bg_modern,
			infoBackgroundToastColor = R.color.ctoast_info_bg_modern,
		)

		/**
		 * Pastel (Soft/Candy): Các màu rất nhẹ, màu sữa cho thẩm mỹ mềm mại.
		 */
		val Pastel = CToastConfiguration(
			successToastColor = R.color.ctoast_success_pastel,
			errorToastColor = R.color.ctoast_error_pastel,
			warningToastColor = R.color.ctoast_warning_pastel,
			infoToastColor = R.color.ctoast_info_pastel,
			successBackgroundToastColor = R.color.ctoast_success_bg_pastel,
			errorBackgroundToastColor = R.color.ctoast_error_bg_pastel,
			warningBackgroundToastColor = R.color.ctoast_warning_bg_pastel,
			infoBackgroundToastColor = R.color.ctoast_info_bg_pastel,
		)

		/**
		 * Vivid (Neon/Cyberpunk): Màu Magenta và Cyan độ bão hòa cao để tạo ấn tượng.
		 */
		val Vivid = CToastConfiguration(
			successToastColor = R.color.ctoast_success_vivid,
			errorToastColor = R.color.ctoast_error_vivid,
			warningToastColor = R.color.ctoast_warning_vivid,
			infoToastColor = R.color.ctoast_info_vivid,
			successBackgroundToastColor = R.color.ctoast_success_bg_vivid,
			errorBackgroundToastColor = R.color.ctoast_error_bg_vivid,
			warningBackgroundToastColor = R.color.ctoast_warning_bg_vivid,
			infoBackgroundToastColor = R.color.ctoast_info_bg_vivid,
		)

		/**
		 * Earth (Natural/Organic): Các tông màu tự nhiên như Olive và Terracotta.
		 */
		val Earth = CToastConfiguration(
			successToastColor = R.color.ctoast_success_earth,
			errorToastColor = R.color.ctoast_error_earth,
			warningToastColor = R.color.ctoast_warning_earth,
			infoToastColor = R.color.ctoast_info_earth,
			successBackgroundToastColor = R.color.ctoast_success_bg_earth,
			errorBackgroundToastColor = R.color.ctoast_error_bg_earth,
			warningBackgroundToastColor = R.color.ctoast_warning_bg_earth,
			infoBackgroundToastColor = R.color.ctoast_info_bg_earth,
		)

		/**
		 * Nord (Arctic/Cool): Bảng màu xám xanh tối giản, chuyên nghiệp.
		 */
		val Nord = CToastConfiguration(
			successToastColor = R.color.ctoast_success_nord,
			errorToastColor = R.color.ctoast_error_nord,
			warningToastColor = R.color.ctoast_warning_nord,
			infoToastColor = R.color.ctoast_info_nord,
			successBackgroundToastColor = R.color.ctoast_success_bg_nord,
			errorBackgroundToastColor = R.color.ctoast_error_bg_nord,
			warningBackgroundToastColor = R.color.ctoast_warning_bg_nord,
			infoBackgroundToastColor = R.color.ctoast_info_bg_nord,
		)

		/**
		 * Material Light: Nền Material Design sạch sẽ với các biểu tượng có độ tương phản cao.
		 */
		val Material = CToastConfiguration(
			successToastColor = R.color.success_color,
			errorToastColor = R.color.error_color,
			warningToastColor = R.color.warning_color,
			infoToastColor = R.color.info_color,
			successBackgroundToastColor = R.color.toast_success_bg,
			errorBackgroundToastColor = R.color.toast_error_bg,
			warningBackgroundToastColor = R.color.toast_warning_bg,
			infoBackgroundToastColor = R.color.toast_info_bg,
		)
	}
}

/**
 * Data class lưu trữ các thuộc tính kiểu dáng đã được phân giải cho một toast cụ thể.
 */
@Immutable
internal data class StyleSpec(
	@param:DrawableRes val iconRes: Int,
	@param:ColorRes val iconColor: Int,
	@param:ColorRes val backgroundColor: Int,
	val defaultTitle: String
)

//enum class CToastPosition { TOP, BOTTOM, CENTER }
enum class CToastType { SUCCESS, ERROR, WARNING, INFO }
enum class CToastLayout { Fill, Outlined, Gradient, Stacked/*, IconRight*/ }

const val DURATION_SHORT = 1800L
const val DURATION_LENGTH = 3400L

/**
 * CompositionLocal để cung cấp CToastConfiguration xuống cây giao diện người dùng.
 * Sử dụng cái này để ghi đè cấu hình mặc định cho một phần cụ thể trong ứng dụng của bạn.
 */
val LocalCToastConfig = staticCompositionLocalOf { CToastConfiguration.Modern }
val LocalToast = compositionLocalOf { CToastState() }