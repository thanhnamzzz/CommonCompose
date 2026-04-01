package common.commons_compose.bottomNavigationBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
	onDetailClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier,
	) {
		TopAppBar(
			title = {
				Text("Home")
			},
			windowInsets = WindowInsets(0),
		)
		Spacer(Modifier.weight(8f))
		Text("Home")
		Spacer(Modifier.weight(1f))
		Button(onClick = onDetailClick) {
			Text("Go to detail")
		}
		Spacer(Modifier.weight(8f))
	}
}

@Preview(showBackground = true)
@Composable
private fun NoteListScreenPreview() {
	HomeScreen(
		onDetailClick = {},
		modifier = Modifier.fillMaxSize()
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDetailScreen(
	onBackClick: () -> Unit,
	modifier: Modifier = Modifier,
) {

	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		modifier = modifier
			.verticalScroll(rememberScrollState())
	) {
		TopAppBar(
			title = {
				Text("Home detail")
			},
			navigationIcon = {
				IconButton(onClick = onBackClick) {
					Icon(
						imageVector = Icons.AutoMirrored.Default.ArrowBack,
						contentDescription = "Back"
					)
				}
			},
			windowInsets = WindowInsets(0),
		)

		Text(
			text = "This is the home detail screen",
			style = MaterialTheme.typography.bodyLarge,
			modifier = Modifier.padding(horizontal = 16.dp)
		)
	}
}

@Preview(showBackground = true)
@Composable
private fun HomeDetailScreenPreview() {
	HomeDetailScreen(
		onBackClick = {},
		modifier = Modifier
			.fillMaxSize()
	)
}