package common.libs.compose.stackManager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.navigation3.runtime.NavKey

/**
 * Bộ quản lý điều hướng đa luồng (Multi-Stack) cho Navigation3.
 *
 * @param startKey Màn hình gốc mặc định của ứng dụng (thường là Home).
 * @param persistentStartKey Nếu true, stack của [startKey] luôn nằm dưới cùng của mọi stack khác.
 *                           Hành vi này giúp nhấn Back ở các Tab khác sẽ quay về Home trước khi thoát.
 */
class MultiStackNavManager<T : NavKey>(
	private val startKey: T,
	private val persistentStartKey: Boolean = true,
	initialStacks: Map<T, List<T>>? = null,
	initialTopLevelKey: T? = null
) {
	private val stacks = mutableStateMapOf<T, SnapshotStateList<T>>().apply {
		if (initialStacks != null) {
			initialStacks.forEach { (key, list) ->
				put(key, list.toMutableStateList())
			}
		} else {
			put(startKey, mutableStateListOf(startKey))
		}
	}

	/**
	 * Định danh của nhánh điều hướng hiện tại (ví dụ: Tab đang chọn).
	 */
	var currentTopLevelKey by mutableStateOf(initialTopLevelKey ?: startKey)
		private set

	/**
	 * Danh sách backstack tổng hợp để truyền vào [NavDisplay].
	 */
	val backStack = mutableStateListOf<T>()

	init {
		updateBackStack()
	}

	/**
	 * Cập nhật lại backStack tổng hợp dựa trên nhánh hiện tại và cấu hình persistentStartKey.
	 */
	private fun updateBackStack() {
		backStack.clear()
		val currentStack = stacks[currentTopLevelKey] ?: emptyList()

		if (!persistentStartKey || currentTopLevelKey == startKey) {
			backStack.addAll(currentStack)
		} else {
			val startStack = stacks[startKey] ?: emptyList()
			backStack.addAll(startStack + currentStack)
		}
	}

	/**
	 * Chuyển đổi giữa các nhánh điều hướng (ví dụ: nhấn vào BottomBar Item).
	 * Cải tiến: Nếu nhấn lại vào tab hiện tại, pop về root của tab đó.
	 */
	fun switchStack(key: T) {
		if (currentTopLevelKey == key) {
			val stack = stacks[key]
			if (stack != null && stack.size > 1) {
				val root = stack[0]
				stack.clear()
				stack.add(root)
			}
		} else {
			if (stacks[key] == null) {
				stacks[key] = mutableStateListOf(key)
			}
			currentTopLevelKey = key
		}
		updateBackStack()
	}

	/**
	 * Thêm màn hình mới vào nhánh hiện tại (Điều hướng sâu).
	 */
	fun push(key: T) {
		stacks[currentTopLevelKey]?.add(key)
		updateBackStack()
	}

	/**
	 * Xử lý quay lại (Back).
	 * 1. Nếu nhánh hiện tại có nhiều hơn 1 màn hình: Xóa màn hình trên cùng.
	 * 2. Nếu đang ở gốc của nhánh khác: Quay về nhánh [startKey] (nếu persistentStartKey = true).
	 */
	fun pop() {
		val currentStack = stacks[currentTopLevelKey] ?: return

		if (currentStack.size > 1) {
			currentStack.removeAt(currentStack.size - 1)
		} else if (persistentStartKey && currentTopLevelKey != startKey) {
			currentTopLevelKey = startKey
		}
		updateBackStack()
	}

	/**
	 * Điều hướng trực tiếp đến một màn hình trong một stack cụ thể (Deep Linking).
	 */
	fun handleDeepLink(stackKey: T, screenKey: T) {
		if (stacks[stackKey] == null) {
			stacks[stackKey] = mutableStateListOf(stackKey)
		}
		if (stackKey != screenKey && stacks[stackKey]?.contains(screenKey) != true) {
			stacks[stackKey]?.add(screenKey)
		}
		currentTopLevelKey = stackKey
		updateBackStack()
	}

	/**
	 * Thay thế toàn bộ stack của nhánh hiện tại.
	 */
	fun replaceStack(vararg keys: T) {
		stacks[currentTopLevelKey] = mutableStateListOf(*keys)
		updateBackStack()
	}

	/**
	 * Reset toàn bộ ứng dụng về trạng thái ban đầu.
	 */
	fun clearAllAndReset() {
		stacks.clear()
		stacks[startKey] = mutableStateListOf(startKey)
		currentTopLevelKey = startKey
		updateBackStack()
	}
}

/**
 * Hàm remember chuyên dụng để khởi tạo MultiStackNavManager.
 */
@Composable
fun <T : NavKey> rememberMultiStackNavManager(
	startKey: T,
	persistentStartKey: Boolean = true
): MultiStackNavManager<T> {
	return remember(startKey, persistentStartKey) {
		MultiStackNavManager(startKey, persistentStartKey)
	}
}
