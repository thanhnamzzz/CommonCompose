package common.commons_compose

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import common.libs.compose.toast.CToastConfiguration
import common.libs.compose.toast.CToastHost
import common.libs.compose.toast.CToastState
import common.libs.compose.toast.CToastType
import common.libs.compose.toast.LocalCToastConfig
import kotlinx.coroutines.launch

@Composable
fun ToastScreen(
	modifier: Modifier = Modifier,
	context: Context
) {
	/** Khai báo */
	val cToastState = remember { CToastState() }
	val scope = rememberCoroutineScope()

	var isDarkMode by remember { mutableStateOf(false) }
	val scrollState = rememberScrollState()

	/** Cài đặt provider */
	CompositionLocalProvider(LocalCToastConfig provides CToastConfiguration()) {
		Column(
			modifier = modifier
				.fillMaxSize()
				.verticalScroll(scrollState),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.Center
			) {
				Switch(
					checked = isDarkMode,
					onCheckedChange = { isDarkMode = it }
				)
				Text(
					text = "Dark Mode",
					modifier = Modifier.padding(start = 8.dp),
					color = MaterialTheme.colorScheme.onBackground
				)
			}
			Button(
				onClick = {
					/** Show */
					scope.launch {
						cToastState.setAndShow(
							title = context.getString(R.string.app_name),
							message = context.getString(R.string.success),
							type = CToastType.SUCCESS,
							isDarkMode = isDarkMode,
						)
					}
				},
				shape = RectangleShape,
				colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
				modifier = Modifier.fillMaxWidth()
			) {
				Text(
					text = stringResource(R.string.success).uppercase(),
					color = MaterialTheme.colorScheme.onPrimary
				)
			}
			Button(
				onClick = {
					scope.launch {
						cToastState.setAndShow(
							title = context.getString(R.string.app_name),
							message = "Message Error",
							type = CToastType.ERROR,
							isDarkMode = isDarkMode,
						)
					}
				},
				shape = RectangleShape,
				colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
				modifier = Modifier.fillMaxWidth()
			) {
				Text(
					text = stringResource(R.string.error).uppercase(),
					color = MaterialTheme.colorScheme.onError
				)
			}
			Button(
				onClick = {
					scope.launch {
						cToastState.setAndShow(
							title = context.getString(R.string.app_name),
							message = "Message Warning",
							type = CToastType.WARNING,
							isDarkMode = isDarkMode,
						)
					}
				},
				shape = RectangleShape,
				colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
				modifier = Modifier.fillMaxWidth()
			) {
				Text(
					text = stringResource(R.string.warning).uppercase(),
					color = MaterialTheme.colorScheme.onTertiary
				)
			}
			Button(
				onClick = {
					scope.launch {
						cToastState.setAndShow(
							title = context.getString(R.string.app_name),
							message = "Message Info",
							type = CToastType.INFO,
							isDarkMode = isDarkMode,
						)
					}
				},
				shape = RectangleShape,
				colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
				modifier = Modifier.fillMaxWidth()
			) {
				Text(
					text = stringResource(R.string.info).uppercase(),
					color = MaterialTheme.colorScheme.onSecondary
				)
			}
		}

		/** Show */
		CToastHost(hostState = cToastState, modifier = Modifier.systemBarsPadding())
	}
}