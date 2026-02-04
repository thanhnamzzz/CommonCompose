package common.commons_compose

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {
	@Serializable
	data object Home : Screen, NavKey
	@Serializable
	data object Dialog : Screen, NavKey
	@Serializable
	data object CropImage : Screen, NavKey
	@Serializable
	data object Toast : Screen, NavKey
	@Serializable
	data object LiquidGlass : Screen, NavKey
	@Serializable
	data object LazyColumnLiquid : Screen, NavKey
	@Serializable
	data object ColorPicker : Screen, NavKey
	@Serializable
	data object ImagePicker : Screen, NavKey
	@Serializable
	data object AnimationView : Screen, NavKey
}