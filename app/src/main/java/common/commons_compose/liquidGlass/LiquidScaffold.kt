package common.commons_compose.liquidGlass

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

@Composable
fun LiquidViewScaffold(
	modifier: Modifier = Modifier,
	background: Int? = null,
	content: @Composable BoxScope.(backdrop: LayerBackdrop) -> Unit
) {
	Box(
		modifier = modifier.fillMaxSize()
	) {
		val backdrop = rememberLayerBackdrop()
		if (background != null) {
			Image(
				painter = painterResource(background),
				contentDescription = null,
				modifier = Modifier
					.layerBackdrop(backdrop)
					.then(modifier)
					.fillMaxSize(),
				contentScale = ContentScale.Crop
			)
		}
		content(backdrop)
	}
}