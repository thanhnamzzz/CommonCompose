package common.libs.compose.liquidGlass

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

@Composable
fun LiquidContainer(
    modifier: Modifier = Modifier,
    background: Int? = null,
    backgroundColor: Color? = null,
    content: @Composable BoxScope.(backdrop: LayerBackdrop) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (backgroundColor != null) Modifier.background(backgroundColor) else Modifier
            )
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