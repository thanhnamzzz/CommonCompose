package common.commons_compose.liquidGlass

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import common.commons_compose.R
import common.commons_compose.viewModels.LiquidLazyViewModel
import common.libs.compose.liquidGlass.LiquidContainer
import common.libs.compose.liquidGlass.components.LiquidButton

@Preview(showBackground = true)
@Composable
fun LiquidLazyColumn(
    modifier: Modifier = Modifier,
    viewModel: LiquidLazyViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.testFunction()
    }
    LiquidContainer(
        modifier = modifier.fillMaxSize()
    ) { liquidDrop ->
        Box(modifier = modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .layerBackdrop(liquidDrop),
                contentPadding = PaddingValues(16f.dp),
                verticalArrangement = Arrangement.spacedBy(16f.dp)
            ) {
                item {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.systemBars))
                }
                items(100) {
                    Image(
                        modifier = modifier
                            .height(160.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp)),
                        painter = painterResource(R.mipmap.wallpaper_light),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
//                    Box(
//                        Modifier
//                            .drawBackdrop(
//                                backdrop = liquidDrop,
//                                shape = { RoundedCornerShape(32.dp) },
//                                effects = {
//                                    vibrancy()
//                                    blur(radius = 15.dp.toPx())
//                                    lens(16f.dp.toPx(), 32f.dp.toPx())
//                                }
//                            )
//                            .height(160f.dp)
//                            .fillMaxWidth()
//                    )
                }
                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
                }
            }

            Column(
                modifier = modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LiquidButton(
                    onClick = {},
                    backdrop = liquidDrop,
                    shape = RoundedCornerShape(15.dp),
                    buttonHeight = 60.dp,
                    paddingHorizontal = 60.dp,
                    blurRadius = 20.dp,
//                    refractionHeight = 30.dp,
//                    refractionAmount = 55.dp
                ) {
                    Text(
                        "Liquid Button 1",
                        color = Color.Black
                    )
                }

                BottomTab(
                    modifier = Modifier.fillMaxWidth(),
                    liquidDrop = liquidDrop,
                    onTabSelected = {},
//                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    }
}