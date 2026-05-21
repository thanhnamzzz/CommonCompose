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
 * Configuration for Compose NiceToast components.
 *
 * This class is marked as @Immutable to help the Compose compiler optimize recompositions.
 * It follows a builder-like pattern where you create a modified copy of the configuration
 * instead of changing its properties directly.
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
	 * Internal function to resolve the style based on the toast type and theme.
	 * This logic is now part of the immutable configuration object.
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
		 * Classic: Material Design 2 Standard colors.
		 */
		val Classic = CToastConfiguration()

		/**
		 * Modern (Slate/Professional): Clean, professional Teal and Indigo tones.
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
		 * Pastel (Soft/Candy): Very light, milky colors for a soft aesthetic.
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
		 * Vivid (Neon/Cyberpunk): High-saturation Magenta and Cyan for impact.
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
		 * Earth (Natural/Organic): Grounded tones like Olive and Terracotta.
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
		 * Nord (Arctic/Cool): A minimalist, professional blue-grey palette.
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
		 * Material Light: Clean Material Design backgrounds with high-contrast icons.
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
 * Data class holding the resolved style properties for a specific toast.
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
 * CompositionLocal to provide the NiceToastConfiguration down the UI tree.
 * Use this to override the default configuration for a specific part of your app.
 */
val LocalCToastConfig = staticCompositionLocalOf { CToastConfiguration.Modern }
val LocalToast = compositionLocalOf { CToastState() }