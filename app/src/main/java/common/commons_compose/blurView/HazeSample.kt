package common.commons_compose.blurView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import common.commons_compose.R
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.CupertinoMaterials
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.FluentMaterials
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState

@Preview(showBackground = true)
@OptIn(ExperimentalHazeMaterialsApi::class, ExperimentalHazeApi::class)
@Composable
fun HazeSample(
	modifier: Modifier = Modifier
) {
	val hazeState = rememberHazeState()
	Box(
		modifier = modifier
			.fillMaxSize()
	) {
		LazyColumn(
			modifier = Modifier.fillMaxSize(),
			contentPadding = PaddingValues(10f.dp),
			verticalArrangement = Arrangement.spacedBy(10f.dp)
		) {
			item {
				Spacer(Modifier.windowInsetsTopHeight(WindowInsets.systemBars))
			}
			items(100) {
				AsyncImage(
					modifier = modifier
						.height(160.dp)
						.fillMaxWidth()
						.clip(RoundedCornerShape(20.dp))
						.hazeSource(hazeState, zIndex = 0f),
					model = R.mipmap.background_home_2,
					contentDescription = "",
					contentScale = ContentScale.Crop,
				)
//				Image(
//					modifier = modifier
//						.height(160.dp)
//						.fillMaxWidth()
//						.clip(RoundedCornerShape(20.dp))
//						.hazeSource(hazeState, zIndex = 0f),
//					painter = painterResource(R.mipmap.wallpaper_light),
//					contentDescription = "",
//					contentScale = ContentScale.Crop
//				)
			}
			item {
				Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
			}
		}

		Column(modifier = Modifier.fillMaxSize()) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(100.dp)
//					.padding(horizontal = 20.dp)
					.clip(RoundedCornerShape(30.dp))
//					.background(Color.White)
					.hazeEffect(
						state = hazeState,
//						style = HazeMaterials.ultraThin().copy(backgroundColor = Color.Black)
						style = HazeMaterials.ultraThin(containerColor = Color.Yellow)
					)
			)

			Spacer(Modifier.size(10.dp))
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(100.dp)
					.background(Color.White)
					.hazeEffect(
						state = hazeState,
//						style = HazeMaterials.ultraThin().copy(backgroundColor = Color.Black)
						style = HazeMaterials.ultraThin()
					)
			)

			Spacer(Modifier.size(10.dp))
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(100.dp)
//					.background(Color.Transparent)
					.hazeEffect(state = hazeState) {
						progressive = HazeProgressive.verticalGradient(
							startIntensity = 1f, endIntensity = 0f,
							preferPerformance = true
						)
					}
			)

			Spacer(Modifier.size(10.dp))
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(100.dp)
//					.padding(horizontal = 20.dp)
					.clip(RoundedCornerShape(20.dp))
					.background(Color.White)
					.hazeEffect(state = hazeState, style = CupertinoMaterials.ultraThin())
			)
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(100.dp)
//					.padding(horizontal = 20.dp)
					.clip(RoundedCornerShape(20.dp))
					.background(Color.White)
					.hazeEffect(
						state = hazeState,
						style = FluentMaterials.thinAcrylic().copy(backgroundColor = Color.White)
					)
			)
		}
	}
}