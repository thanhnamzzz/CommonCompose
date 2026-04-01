package common.commons_compose.bottomNavigationBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

data class Note(
	val id: Long,
	val title: String,
	val content: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCreateScreen(
	onBackClick: () -> Unit,
	onNoteCreated: (id: Long) -> Unit,
	modifier: Modifier = Modifier
) {
	var title by rememberSaveable { mutableStateOf("") }
	var content by rememberSaveable { mutableStateOf("") }

	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		modifier = modifier
			.verticalScroll(rememberScrollState())
	) {
		TopAppBar(
			title = {
				Text("Create Note")
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

		OutlinedTextField(
			value = title,
			onValueChange = { title = it },
			label = { Text("Title") },
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth()
		)

		OutlinedTextField(
			value = content,
			onValueChange = { content = it },
			label = { Text("Content") },
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth()
				.height(200.dp),
			maxLines = Int.MAX_VALUE
		)

		Button(
			onClick = {
				val note = NoteRepository.createNote(title, content)
				onNoteCreated(note.id)
			},
			enabled = title.isNotBlank() && content.isNotBlank(),
			modifier = Modifier.align(Alignment.CenterHorizontally)
		) {
			Text("Save Note")
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun NoteCreateScreenPreview() {
	NoteCreateScreen(
		onBackClick = {},
		onNoteCreated = {},
		modifier = Modifier.fillMaxSize(),
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
	noteId: Long,
	onBackClick: () -> Unit,
	onEditClick: () -> Unit,
	modifier: Modifier = Modifier,
	showBackButton: Boolean = true,
) {
	val note by NoteRepository.getNoteById(noteId).collectAsStateWithLifecycle(null)

	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		modifier = modifier
			.verticalScroll(rememberScrollState())
	) {
		TopAppBar(
			title = {
				Text(note?.title.orEmpty())
			},
			navigationIcon = {
				if (showBackButton) {
					IconButton(onClick = onBackClick) {
						Icon(
							imageVector = Icons.AutoMirrored.Default.ArrowBack,
							contentDescription = "Back"
						)
					}
				}
			},
			windowInsets = WindowInsets(0),
		)
		if (note == null) {

			Text(
				text = "Note not found.",
				modifier = modifier
					.wrapContentSize(Alignment.Center)
			)
		} else {
			Text(
				text = note?.content.orEmpty(),
				style = MaterialTheme.typography.bodyLarge,
				modifier = Modifier.padding(horizontal = 16.dp)
			)
			Button(
				onClick = onEditClick,
				modifier = Modifier.align(Alignment.CenterHorizontally)
			) {
				Text("Edit Note")
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun NoteScreenPreview() {
	NoteDetailScreen(
		noteId = 1L,
		onBackClick = {},
		onEditClick = {},
		modifier = Modifier
			.fillMaxSize()
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
	noteId: Long,
	onBackClick: () -> Unit,
	onSaveClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val originalNote = remember(noteId) { NoteRepository.getNoteByIdImmediate(noteId) }

	var title by rememberSaveable { mutableStateOf(originalNote?.title.orEmpty()) }
	var content by rememberSaveable { mutableStateOf(originalNote?.content.orEmpty()) }

	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		modifier = modifier
			.verticalScroll(rememberScrollState())
	) {
		TopAppBar(
			title = {
				Text("Edit note")
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
		if (originalNote == null) {
			Text(
				text = "Note not found.",
				modifier = modifier.wrapContentSize(Alignment.Center)
			)
		} else {

			OutlinedTextField(
				value = title,
				onValueChange = { title = it },
				label = { Text("Title") },
				modifier = Modifier
					.padding(horizontal = 16.dp)
					.fillMaxWidth()
			)

			OutlinedTextField(
				value = content,
				onValueChange = { content = it },
				label = { Text("Content") },
				modifier = Modifier
					.padding(horizontal = 16.dp)
					.fillMaxWidth()
					.height(200.dp),
				maxLines = Int.MAX_VALUE
			)

			Button(
				onClick = {
					NoteRepository.updateNote(originalNote.copy(title = title, content = content))
					onSaveClick()
				},
				modifier = Modifier.align(Alignment.CenterHorizontally)
			) {
				Text("Save")
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun NoteEditScreenPreview() {
	NoteEditScreen(
		noteId = 1L,
		onBackClick = {},
		onSaveClick = {},
		modifier = Modifier.fillMaxSize()
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
	onNoteClick: (Long) -> Unit,
	onCreateClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val notes by NoteRepository.notes.collectAsStateWithLifecycle(emptyList())

	Column(modifier) {
		TopAppBar(
			title = {
				Text("Notes")
			},
			actions = {
				IconButton(onClick = onCreateClick) {
					Icon(
						imageVector = Icons.Default.Add,
						contentDescription = "Create note"
					)
				}
			},
			windowInsets = WindowInsets(0),
		)
		LazyColumn(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			contentPadding = PaddingValues(16.dp),
			modifier = Modifier.fillMaxSize(),
		) {
			items(notes) { note ->
				Card(
					onClick = { onNoteClick(note.id) },
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 4.dp),
				) {
					Column(
						verticalArrangement = Arrangement.spacedBy(4.dp),
						modifier = Modifier.padding(16.dp)
					) {
						Text(
							text = note.title,
							style = MaterialTheme.typography.titleMedium
						)
						Text(
							text = note.content.take(100),
							style = MaterialTheme.typography.bodyMedium,
							maxLines = 2,
							overflow = TextOverflow.Ellipsis
						)
					}
				}
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun NoteListScreenPreview() {
	NoteListScreen(
		onCreateClick = {},
		onNoteClick = {},
		modifier = Modifier.fillMaxSize()
	)
}