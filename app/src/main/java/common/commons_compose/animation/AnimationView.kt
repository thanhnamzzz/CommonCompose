package common.commons_compose.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimationView(
	modifier: Modifier = Modifier
) {
	var visible by remember { mutableStateOf(true) }

	Column(modifier = modifier) {
		Button(onClick = { visible = !visible }) {
			Text(text = if (visible) "Hide" else "Show")
		}
		/** 1.Hiệu ứng xuất hiện (Enter Transition): Bạn có thể tùy chỉnh cách Composable xuất hiện trên màn hình. Các hiệu ứng có sẵn bao gồm:
		- fadeIn(): Hiệu ứng mờ dần (hiện ra).
		- slideInHorizontally() / slideInVertically(): Hiệu ứng trượt vào từ các cạnh.
		- expandHorizontally() / expandVertically() / expandIn(): Hiệu ứng mở rộng từ một điểm hoặc một cạnh.
		- scaleIn(): Phóng to từ kích thước 0.
		2.Hiệu ứng biến mất (Exit Transition): Tương tự, bạn có thể tùy chỉnh cách Composable biến mất.
		- fadeOut(): Hiệu ứng mờ dần (biến mất).
		- slideOutHorizontally() / slideOutVertically(): Hiệu ứng trượt ra khỏi các cạnh.
		- shrinkHorizontally() / shrinkVertically() / shrinkOut(): Hiệu ứng thu nhỏ lại.
		- scaleOut(): Thu nhỏ về kích thước 0.*/

		AnimatedVisibility(
			visible = visible,
			enter = slideInVertically(animationSpec = tween(durationMillis = 300)) { +it }
					+ fadeIn(animationSpec = tween(durationMillis = 300)),
			exit = slideOutVertically(animationSpec = tween(durationMillis = 300)) { +it }
					+ fadeOut(animationSpec = tween(durationMillis = 300))
		) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(50.dp)
					.background(Color.Red)
			)
		}

		Spacer(Modifier.size(50.dp))

		AnimatedVisibility(
			visible = visible,
			enter = expandHorizontally(
				animationSpec = tween(durationMillis = 300),
				expandFrom = Alignment.Start
			),
			exit = shrinkHorizontally(
				animationSpec = tween(durationMillis = 300),
				shrinkTowards = Alignment.Start
			)
		) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(50.dp)
					.background(Color.Green)
			)
		}

		Spacer(Modifier.size(50.dp))

		AnimatedVisibility(
			visible = visible,
			enter = expandIn(animationSpec = tween(durationMillis = 300)),
			exit = shrinkOut(animationSpec = tween(durationMillis = 300))
		) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(50.dp)
					.background(Color.Blue)
			)
		}

		Spacer(Modifier.size(50.dp))

		AnimatedVisibility(
			visible = visible,
			enter = scaleIn(animationSpec = tween(durationMillis = 300)),
			exit = scaleOut(animationSpec = tween(durationMillis = 300))
		) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(50.dp)
					.background(Color.Blue)
			)
		}
	}
}
