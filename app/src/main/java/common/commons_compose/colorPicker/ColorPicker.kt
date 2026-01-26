package common.commons_compose.colorPicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun ColorPicker() {
	val controller = rememberColorPickerController()
	var result by remember { mutableStateOf("") }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(all = 30.dp)
	) {
		Box(
			modifier = Modifier.fillMaxWidth()
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
		HsvColorPicker(
			modifier = Modifier
				.fillMaxWidth()
				.height(450.dp)
				.padding(10.dp),
			controller = controller,
			onColorChanged = {
				result = it.hexCode
			}
		)
		AlphaSlider(
			modifier = Modifier
				.fillMaxWidth()
				.padding(10.dp)
				.height(35.dp),
			controller = controller,
			tileOddColor = Color.White,
			tileEvenColor = Color.Black
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