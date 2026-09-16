package common.libs.compose.extensions

import android.view.Window
import android.view.WindowInsetsController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import common.libs.extensions.isQ29Plus
import common.libs.extensions.isR30Plus

@Composable
fun Window.SetStatusBarContentColor(backgroundColor: Color) {
	val insetsController = WindowCompat.getInsetsController(this, decorView)

	val isLightBackground = backgroundColor.luminance() > 0.5f

	SideEffect {
		if (isLightBackground) {
			// Background sáng → icon dark
			insetsController.isAppearanceLightStatusBars = true
		} else {
			// Background tối → icon light
			insetsController.isAppearanceLightStatusBars = false
		}
	}
}

@Composable
fun Window.SetNavigationBarContentColor(
	backgroundColor: Color,
	enforceContrast: Boolean = false,
) {
	val controllers = WindowCompat.getInsetsController(this, decorView)

	val isLightBackground = backgroundColor.luminance() > 0.5f

	SideEffect {
		// (3) Đổi màu icon/navigation bar
		controllers.isAppearanceLightNavigationBars = isLightBackground
		// (1) Set navigation bar background
		if (enforceContrast) {
			if (isR30Plus()) {
				if (isLightBackground) {
					insetsController?.apply {
						setSystemBarsAppearance(
							0,
							WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
						)
					}
				} else {
					insetsController?.apply {
						setSystemBarsAppearance(
							WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
							WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
						)
					}
				}
			} else {
				navigationBarColor = backgroundColor.toArgb()
			}
		}
		// (2) Bật / tắt contrast enforcement
		if (isQ29Plus()) {
			isNavigationBarContrastEnforced = enforceContrast
		}
	}
}