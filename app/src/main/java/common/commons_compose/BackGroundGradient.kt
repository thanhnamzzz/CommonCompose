package common.commons_compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScreenGradient() {
	Box(Modifier
		.fillMaxSize()
		.background(Color(0xFF141414))) {
		Image(
			painter = painterResource(R.drawable.bg_blue),
			contentDescription = "",
			contentScale = ContentScale.Crop,
			modifier = Modifier
				.fillMaxSize()
				.blur(
					radius = 300.dp,
					edgeTreatment = BlurredEdgeTreatment.Unbounded
				)
		)
	}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScreenGradientPreview() {
	ScreenGradient()
}