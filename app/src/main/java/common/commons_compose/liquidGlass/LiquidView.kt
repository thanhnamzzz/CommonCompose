package common.commons_compose.liquidGlass

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.colorControls
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.backdrop.highlight.Highlight
import common.commons_compose.LoremIpsum
import common.commons_compose.R
import common.libs.compose.extensions.contrastingColor
import common.libs.compose.liquidGlass.LiquidContainer
import common.libs.compose.liquidGlass.components.LiquidBottomTab
import common.libs.compose.liquidGlass.components.LiquidBottomTabs
import common.libs.compose.liquidGlass.components.LiquidButton
import common.libs.compose.liquidGlass.components.LiquidSlider
import common.libs.compose.liquidGlass.components.LiquidToggle
import common.libs.compose.liquidGlass.components.rememberRectBackdrop
import common.libs.compose.toast.CToastConfiguration
import common.libs.compose.toast.CToastHost
import common.libs.compose.toast.CToastState
import common.libs.compose.toast.CToastType
import common.libs.compose.toast.DURATION_SHORT
import common.libs.compose.toast.LocalCToastConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LiquidView(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    context: Context,
    openLazyColumn: () -> Unit
) {
    val cToastState = remember { CToastState() }
    val scope = rememberCoroutineScope()
    var selected by rememberSaveable { mutableStateOf(false) }

    val isLightTheme = !isSystemInDarkTheme()
    val backgroundColor =
        if (isLightTheme) Color(0xFFFFFFFF)
        else Color(0xFF121212)

    var value by rememberSaveable { mutableFloatStateOf(50f) }
    var showDialog by remember { mutableStateOf(false) }

    LiquidContainer(
        modifier = modifier.fillMaxSize(),
        background = R.mipmap.wallpaper_light
    ) { liquidDrop ->
        CompositionLocalProvider(LocalCToastConfig provides CToastConfiguration()) {
            Box {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Liquid glass Components",
                        style = TextStyle(Color(0xFFE91E63), 20f.sp, FontWeight.Medium)
                    )
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                    LiquidButton(
                        onClick = { showToast(scope, cToastState, context, "1") },
                        backdrop = liquidDrop,
                        shape = RoundedCornerShape(0.dp),
                        buttonHeight = 90.dp,
                        paddingHorizontal = 60.dp
                    ) { Text("Liquid Button 1") }
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                    LiquidButton(
                        onClick = {
//                            showToast(scope, cToastState, context, "2")
                            openLazyColumn()
                        },
                        backdrop = liquidDrop,
                        surfaceColor = Color.Green.copy(0.2f)
                    ) { Text("Lazy Column Liquid") }
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                    LiquidButton(
                        onClick = { showDialog = true },
                        backdrop = liquidDrop,
                        tint = Color(0xFF0088FF)
                    ) { Text("Show Dialog") }
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )

                    LiquidToggle(
                        selected = { selected },
                        onSelect = { selected = it },
                        backdrop = liquidDrop,
                    )
                    Spacer(Modifier.size(5.dp))
                    LiquidToggle(
                        selected = { selected },
                        onSelect = { selected = it },
                        backdrop = liquidDrop,
                        thumbColor = Color(0xFF000540),
                        accentColor = Color(0xFFFCC443),
                        trackColor = Color(0xFFCBCDD3).copy(alpha = 0.7f),
                        paddingTrack = 3.dp,
                        trackHeigh = 28.dp,
                        trackWidth = 56.dp,
                        thumbHeight = 22.dp,
                        thumbWidth = 25.dp,
                        //dragDistance là quãng đường di chuyển của thumb = trackWidth - 2 * paddingTrack - thumbWidth
                        dragDistance = 25.dp,
                        scalePressed = 1.8f
                    )
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )

                    LiquidToggle(
                        selected = { selected },
                        onSelect = { selected = it },
                        backdrop = rememberRectBackdrop { drawRect(backgroundColor) },
//						backdrop = rememberCanvasBackdrop { drawRect(backgroundColor) },
                    )
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                    LiquidSlider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        value = { value },
                        onValueChange = { value = it },
                        valueRange = 0f..100f,
                        backdrop = liquidDrop,
                        trackHeight = 8.dp,
                        accentColor = Color(0xFFFFEB3B),
                        shapeThumb = RoundedCornerShape(30.dp),
                        thumbColor = Color(0xFF03A9F4),
                        thumbColorShadow = Color(0xFF03A9F4).contrastingColor(),
//						scalePressed = 1.2f
                    )
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                    LiquidSlider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        value = { value },
                        onValueChange = { value = it },
                        valueRange = 0f..100f,
                        backdrop = rememberRectBackdrop { drawRect(backgroundColor) },
//						backdrop = rememberCanvasBackdrop { drawRect(backgroundColor) },
                    )
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )

                    BottomTab(
                        modifier = Modifier.fillMaxWidth(),
                        liquidDrop = liquidDrop,
                        onTabSelected = {
                            showToast(scope, cToastState, context, "Selected Tab $it", 800)
                        },
                        shape = RoundedCornerShape(20.dp)
                    )

                    BottomTab(
                        modifier = Modifier.fillMaxWidth(),
                        liquidDrop = liquidDrop,
                        onTabSelected = {
                            showToast(scope, cToastState, context, "Selected Tab $it", 800)
                        },
                        shape = RoundedCornerShape(40.dp),
                        addItem = true
                    )
                }
            }

            if (selected) {
                showToast(scope, cToastState, context, "Selected")
            }

            CToastHost(
                hostState = cToastState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 35.dp, vertical = 50.dp)
            )
        }

        if (showDialog) {
            ShowDialogSample(
                backdrop = liquidDrop,
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun BottomTab(
    modifier: Modifier = Modifier,
    liquidDrop: Backdrop,
    shape: RoundedCornerShape = RoundedCornerShape(50.dp),
    addItem: Boolean = false,
    onTabSelected: (index: Int) -> Unit,
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    LiquidBottomTabs(
        selectedTabIndex = { selectedTabIndex },
        onTabSelected = {
            selectedTabIndex = it
            onTabSelected(selectedTabIndex)
        },
        backdrop = liquidDrop,
        tabsCount = if (addItem) 4 else 3,
        containerColor = Color(0xFFFAFAFA).copy(0.05f),
        accentColor = Color(0xFFFF0000),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 5.dp),
        bottomTabHeight = 80.dp,
        selectedTabHeight = 65.dp,
        shape = shape,
        paddingValues = 10.dp,
        tabPadding = 20.dp,
        tabSelected = Color.Black.copy(0.1f),
//		scalePressed = 1.8f,
        blurRadius = 50.dp,
//        refractionHeight = 45.dp,
//        refractionAmount = 45.dp
    ) {
        LiquidBottomTab(onClick = { selectedTabIndex = 0 }) {
            BottomTab(
                idIcon = R.drawable.icon_anchor,
                title = "Anchor",
                titleSize = 14.sp
            )
        }
        LiquidBottomTab(onClick = { selectedTabIndex = 1 }) {
            BottomTab(
                idIcon = R.drawable.icon_reader,
                title = "Reader",
                titleSize = 14.sp
            )
        }
        LiquidBottomTab(onClick = { selectedTabIndex = 2 }) {
            BottomTab(
                idIcon = R.drawable.icon_color_lens,
                title = "Color Lens",
                titleSize = 14.sp
            )
        }
        if (addItem) {
            LiquidBottomTab(onClick = { selectedTabIndex = 3 }) {
                BottomTab(
                    idIcon = R.drawable.icon_anchor,
                    title = "Anchor",
                    titleSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun BottomTab(
    idIcon: Int,
    title: String = "",
    titleSize: TextUnit = 14.sp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        Image(
            painter = painterResource(idIcon),
            contentDescription = null
        )
        Text(
            text = title,
            fontSize = titleSize,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(5.dp))
    }
}

private fun showToast(
    scope: CoroutineScope,
    cToastState: CToastState,
    context: Context,
    message: String,
    duration: Long = DURATION_SHORT
) {
    scope.launch {
        cToastState.setAndShow(
            title = context.getString(R.string.app_name),
            message = "Liquid Button click $message",
            type = CToastType.SUCCESS,
            duration = duration
        )
    }
}

@Composable
fun ShowDialogSample(
    backdrop: Backdrop,
    onDismiss: () -> Unit
) {
    val isLightTheme = !isSystemInDarkTheme()
    val contentColor = if (isLightTheme) Color.Black else Color.White
    val accentColor =
        if (isLightTheme) Color(0xFF0088FF)
        else Color(0xFF0091FF)
    val containerColor =
        if (isLightTheme) Color(0xFFFFFFFF).copy(0.05f)
        else Color(0xFF121212).copy(0.4f)
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        Column(
            Modifier
//				.padding(40f.dp)
                .drawBackdrop(
                    backdrop = rememberCombinedBackdrop(backdrop, rememberLayerBackdrop()),
                    shape = { RoundedCornerShape(40.dp) },
                    effects = {
                        vibrancy()
                        colorControls(
                            brightness = if (isLightTheme) 0.05f else 0f,
                            saturation = 1.5f
                        )
//						blur(if (isLightTheme) 16f.dp.toPx() else 8f.dp.toPx())
                        blur(8f.dp.toPx())
                        lens(
                            refractionHeight = 55f.dp.toPx(),
                            refractionAmount = 85f.dp.toPx(),
                            depthEffect = true
                        )
                    },
                    highlight = { Highlight.Plain },
                    onDrawSurface = { drawRect(containerColor) }
                )
                .fillMaxWidth()
        ) {
            BasicText(
                "Dialog Title",
                Modifier
                    .padding(28f.dp, 24f.dp, 28f.dp, 12f.dp)
                    .fillMaxWidth(),
                style = TextStyle(contentColor, 24f.sp, FontWeight.Medium)
            )

            BasicText(
                LoremIpsum,
                Modifier
                    .then(
                        if (isLightTheme) {
                            // plus darker
                            Modifier
                        } else {
                            // plus lighter
                            Modifier.graphicsLayer(blendMode = BlendMode.Plus)
                        }
                    )
                    .padding(24f.dp, 12f.dp, 24f.dp, 12f.dp)
                    .fillMaxWidth(),
                style = TextStyle(contentColor.copy(0.68f), 15f.sp),
                maxLines = 5
            )

            Row(
                Modifier
                    .padding(24f.dp, 12f.dp, 24f.dp, 24f.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16f.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(containerColor.copy(0.2f))
                        .clickable {
                            onDismiss()
                        }
                        .height(48f.dp)
                        .weight(1f)
                        .padding(horizontal = 16f.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        4f.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicText(
                        "Cancel",
                        style = TextStyle(contentColor, 16f.sp)
                    )
                }
                Row(
                    Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(accentColor)
                        .clickable {
                            onDismiss()
                        }
                        .height(48f.dp)
                        .weight(1f)
                        .padding(horizontal = 16f.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        4f.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicText(
                        "Okay",
                        style = TextStyle(Color.White, 16f.sp)
                    )
                }
            }
        }
    }
}