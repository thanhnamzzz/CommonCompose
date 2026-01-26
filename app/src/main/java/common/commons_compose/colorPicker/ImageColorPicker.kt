package common.commons_compose.colorPicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ImageColorPicker
import com.github.skydoves.colorpicker.compose.PaletteContentScale
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import common.commons_compose.R

@Composable
fun ImagePicker() {
	val controller = rememberColorPickerController()
	var result by remember { mutableStateOf("") }

	Column(modifier = Modifier.fillMaxSize()) {
		ImageColorPicker(
			modifier = Modifier
				.fillMaxWidth()
				.height(400.dp),
			paletteImageBitmap = ImageBitmap.imageResource(R.mipmap.wallpaper_light),
			controller = controller,
			paletteContentScale = PaletteContentScale.FIT,
			onColorChanged = { colorEnvelope: ColorEnvelope ->
				result = colorEnvelope.hexCode
			}
		)
		Spacer(modifier = Modifier.height(12.dp))
		Box (
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 30.dp),
		) {
			AlphaTile(
				modifier = Modifier
					.fillMaxWidth()
					.height(60.dp)
					.clip(RoundedCornerShape(6.dp)),
				controller = controller
			)
			Text(result, modifier = Modifier.align(Alignment.Center), color = Color.Black)
		}
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 30.dp)
//                .height(60.dp)
//                .background(color = controller.selectedColor.value)
//        ) {}
		AlphaSlider(
			modifier = Modifier
				.fillMaxWidth()
				.padding(10.dp)
				.height(35.dp),
			controller = controller,
//            tileOddColor = Color.White,
//            tileEvenColor = Color.Black
		)
		BrightnessSlider(
			modifier = Modifier
				.fillMaxWidth()
				.padding(10.dp)
				.height(35.dp),
			controller = controller,
		)
	}
}