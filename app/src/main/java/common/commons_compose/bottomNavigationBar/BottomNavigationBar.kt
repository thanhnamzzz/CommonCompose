package common.commons_compose.bottomNavigationBar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import common.commons_compose.bottomNavigationBar.ui.theme.CommonComposeTheme
import common.libs.compose.stackManager.rememberMultiStackNavManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

interface BottomNavItem {
	val icon: ImageVector
	val title: String
}

@Serializable
data object Home2 : NavKey, BottomNavItem {
	override val icon: ImageVector = Icons.Filled.Home
	override val title: String = "Home"
}

@Serializable
data object HomeDetail : NavKey

@Serializable
data object NoteList : NavKey, BottomNavItem {
	override val icon: ImageVector = Icons.AutoMirrored.Filled.Notes
	override val title: String = "Notes"
}

@Serializable
data object NoteCreate : NavKey

@Serializable
data class NoteDetail(val id: Long) : NavKey

@Serializable
data class NoteEdit(val id: Long) : NavKey

@AndroidEntryPoint
class BottomNavigationBar : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			CommonComposeTheme {
				val bottomNavItems = listOf(Home2, NoteList)
//				val topLevelBackStack = remember { TopLevelBackStack<NavKey>(Home) }
				val navManager = rememberMultiStackNavManager<NavKey>(Home2)
//				val navManager = remember { MultiStackNavManager<NavKey>(Home2) }
				Scaffold(
					bottomBar = {
						NavigationBar {
							bottomNavItems.forEach { item ->
//								val selected = topLevelBackStack.topLevelKey == item
								val selected = navManager.currentTopLevelKey == item
								NavigationBarItem(
									selected = selected,
									onClick = {
										navManager.switchStack(item)
//										topLevelBackStack.switchTopLevel(item)
									},
									icon = {
										Icon(
											imageVector = item.icon,
											contentDescription = item.title
										)
									},
									label = {
										Text(item.title)
									},
								)

							}
						}
					},
					modifier = Modifier.fillMaxSize()
				) { innerPadding ->
					val screenModifier = Modifier
						.fillMaxSize()
						.padding(innerPadding)
					NavDisplay(
//						backStack = topLevelBackStack.backStack,
//						onBack = { topLevelBackStack.removeLast() },
						backStack = navManager.backStack,
						onBack = { navManager.pop() },
						entryDecorators = listOf(
							rememberSaveableStateHolderNavEntryDecorator(),
							rememberViewModelStoreNavEntryDecorator()
						),
						entryProvider = entryProvider {
							entry<Home2> {
								HomeScreen(
//									onDetailClick = { topLevelBackStack.add(HomeDetail) },
									onDetailClick = { navManager.push(HomeDetail) },
									modifier = screenModifier
								)
							}
							entry<HomeDetail> {
								HomeDetailScreen(
//									onBackClick = { topLevelBackStack.removeLast() },
									onBackClick = { navManager.pop() },
									modifier = screenModifier
								)
							}
							entry<NoteList> {
								NoteListScreen(
//									onNoteClick = { id -> topLevelBackStack.add(NoteDetail(id)) },
//									onCreateClick = { topLevelBackStack.add(NoteCreate) },
									onNoteClick = { id -> navManager.push(NoteDetail(id)) },
									onCreateClick = { navManager.push(NoteCreate) },
									modifier = screenModifier
								)
							}
							entry<NoteDetail> { args ->
								NoteDetailScreen(
									noteId = args.id,
//									onBackClick = { topLevelBackStack.removeLast() },
//									onEditClick = { topLevelBackStack.add(NoteEdit(args.id)) },
									onBackClick = { navManager.pop() },
									onEditClick = { navManager.push(NoteEdit(args.id)) },
									modifier = screenModifier
								)
							}
							entry<NoteEdit> { args ->
								NoteEditScreen(
									noteId = args.id,
//									onBackClick = { topLevelBackStack.removeLast() },
//									onSaveClick = { topLevelBackStack.removeLast() },
									onBackClick = { navManager.pop() },
									onSaveClick = { navManager.pop() },
									modifier = screenModifier
								)
							}
							entry<NoteCreate> {
								NoteCreateScreen(
									onBackClick = { navManager.pop() },
									onNoteCreated = { id ->
										navManager.replaceStack(NoteList, NoteDetail(id))
									},
									modifier = screenModifier
								)
							}
						}
					)
				}
			}
		}
	}
}

class TopLevelBackStack<T : NavKey>(private val startKey: T) {

	private var topLevelBackStacks: HashMap<T, SnapshotStateList<T>> = hashMapOf(
		startKey to mutableStateListOf(startKey)
	)

	var topLevelKey by mutableStateOf(startKey)
		private set

	val backStack = mutableStateListOf(startKey)

	private fun updateBackStack() {
		backStack.clear()
		val currentStack = topLevelBackStacks[topLevelKey] ?: emptyList()

		if (topLevelKey == startKey) {
			backStack.addAll(currentStack)
		} else {
			val startStack = topLevelBackStacks[startKey] ?: emptyList()
			backStack.addAll(startStack + currentStack)
		}
	}

	fun switchTopLevel(key: T) {
		if (topLevelBackStacks[key] == null) {
			topLevelBackStacks[key] = mutableStateListOf(key)
		}
		topLevelKey = key
		updateBackStack()
	}

	fun add(key: T) {
		topLevelBackStacks[topLevelKey]?.add(key)
		updateBackStack()
	}

	fun removeLast() {
		val currentStack = topLevelBackStacks[topLevelKey] ?: return

		if (currentStack.size > 1) {
			currentStack.removeLastOrNull()
		} else if (topLevelKey != startKey) {
			topLevelKey = startKey
		}
		updateBackStack()
	}

	fun replaceStack(vararg keys: T) {
		topLevelBackStacks[topLevelKey] = mutableStateListOf(*keys)
		updateBackStack()
	}
}