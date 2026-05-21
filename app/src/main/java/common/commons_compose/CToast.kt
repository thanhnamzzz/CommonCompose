package common.commons_compose

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import common.commons_compose.ui.theme.CommonComposeTheme
import common.commons_compose.viewModels.ToastViewModel
import common.libs.compose.toast.CToastConfiguration
import common.libs.compose.toast.CToastLayout
import common.libs.compose.toast.CToastType
import common.libs.compose.toast.DURATION_LENGTH
import common.libs.compose.toast.LocalToast
import kotlinx.coroutines.launch

@Composable
fun ToastScreen(
	modifier: Modifier = Modifier,
	context: Context,
	viewModel: ToastViewModel = hiltViewModel()
) {
	val currentToastStyle = remember { mutableStateOf(CToastConfiguration.Classic) }
	val currentLayoutToast = remember { mutableStateOf(CToastLayout.Fill) }
	LaunchedEffect(Unit) {
		viewModel.testFunction()
	}
	/** Khai báo */
//	val cToastState = remember { CToastState() }
	val scope = rememberCoroutineScope()

	var isDarkMode by remember { mutableStateOf(false) }
	val scrollState = rememberScrollState()

//	/** Cài đặt provider */
//	CompositionLocalProvider(
//		LocalCToastConfig provides CToastConfiguration(),
//		LocalToast provides cToastState
//	) {

	CommonComposeTheme(toastConfig = currentToastStyle.value, toastLayout = currentLayoutToast.value) {
		val ct = LocalToast.current
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
			Text(
				text = "Layout Toast",
				fontSize = 18.sp,
				color = Color.Black
			)
			ListLayout(
				selectLayout = currentLayoutToast.value,
				onLayoutChange = {
					currentLayoutToast.value = it
				}
			)
			Text(
				text = "Config Toast",
				fontSize = 18.sp,
				color = Color.Black
			)
			ListConfig(
				selectedConfig = currentToastStyle.value,
				onConfigSelected = {
					currentToastStyle.value = it
				}
			)
			Button(
				onClick = {
					/** Show */
					scope.launch {
						ct.setAndShow(
							title = context.getString(R.string.app_name),
							message = context.getString(R.string.success),
							type = CToastType.SUCCESS,
							duration = DURATION_LENGTH
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
						ct.setAndShow(
							title = context.getString(R.string.app_name),
							message = "Message Error",
							type = CToastType.ERROR,
							duration = DURATION_LENGTH
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
						ct.setAndShow(
							title = context.getString(R.string.app_name),
							message = "Message Warning",
							type = CToastType.WARNING,
							duration = DURATION_LENGTH
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
						ct.setAndShow(
							title = context.getString(R.string.app_name),
							message = "Message Info",
							type = CToastType.INFO,
							duration = DURATION_LENGTH
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
	}
//		/** Show */
//		CToastHost(hostState = cToastState, modifier = Modifier.systemBarsPadding())
//		CToastHost(modifier = Modifier.systemBarsPadding())
//	}
}

@Preview
@Composable
fun ListLayout(
	modifier: Modifier = Modifier,
	selectLayout: CToastLayout = CToastLayout.Fill,
	onLayoutChange: (CToastLayout) -> Unit = {}
) {
	val layouts = remember {
		listOf(
			"Fill" to CToastLayout.Fill,
			"Gradient" to CToastLayout.Gradient,
			"Outlined" to CToastLayout.Outlined,
		)
	}

	LazyRow(
		modifier = modifier
			.fillMaxWidth()
			.padding(vertical = 12.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		contentPadding = PaddingValues(horizontal = 20.dp)
	) {
		items(layouts) { (name, config) ->
			val isSelected = selectLayout == config

			Button(
				onClick = { onLayoutChange(config) },
				shape = RoundedCornerShape(12.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = if (isSelected) MaterialTheme.colorScheme.primary
					else MaterialTheme.colorScheme.surfaceVariant,
					contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary
					else MaterialTheme.colorScheme.onSurfaceVariant
				),
				elevation = ButtonDefaults.buttonColors().let {
					if (isSelected) ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
					else ButtonDefaults.buttonElevation()
				}
			) {
				Text(
					text = name,
					fontSize = 16.sp,
				)
			}
		}
	}
}

@Composable
fun ListConfig(
	modifier: Modifier = Modifier,
	selectedConfig: CToastConfiguration = CToastConfiguration.Classic,
	onConfigSelected: (CToastConfiguration) -> Unit = {}
) {
	val configs = remember {
		listOf(
			"Classic" to CToastConfiguration.Classic,
			"Modern" to CToastConfiguration.Modern,
			"Pastel" to CToastConfiguration.Pastel,
			"Vivid" to CToastConfiguration.Vivid,
			"Nord" to CToastConfiguration.Nord,
			"Earth" to CToastConfiguration.Earth,
			"Material" to CToastConfiguration.Material
		)
	}

	LazyRow(
		modifier = modifier
			.fillMaxWidth()
			.padding(vertical = 12.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		contentPadding = PaddingValues(horizontal = 20.dp)
	) {
		items(configs) { (name, config) ->
			val isSelected = selectedConfig == config

			Button(
				onClick = { onConfigSelected(config) },
				shape = RoundedCornerShape(12.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = if (isSelected) MaterialTheme.colorScheme.primary
					else MaterialTheme.colorScheme.surfaceVariant,
					contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary
					else MaterialTheme.colorScheme.onSurfaceVariant
				),
				elevation = ButtonDefaults.buttonColors().let {
					if (isSelected) ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
					else ButtonDefaults.buttonElevation()
				}
			) {
				Text(
					text = name,
					fontSize = 16.sp,
				)
			}
		}
	}
}