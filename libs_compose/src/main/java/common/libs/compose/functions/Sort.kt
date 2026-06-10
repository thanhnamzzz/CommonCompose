package common.libs.compose.functions

//fun <T, R : Comparable<R>> sortByProperty(
//    list: Iterable<T>,
//    isAscending: Boolean = true,
//    propertySelector: (T) -> R,
//): List<T> {
//    return if (isAscending) {
//        list.sortedWith(compareBy { propertySelector(it) })
//    } else {
//        list.sortedWith(compareByDescending { propertySelector(it) })
//    }
//}

inline fun <T, R : Comparable<R>> Iterable<T>.sortByProperty(
	isAscending: Boolean = true,
	crossinline propertySelector: (T) -> R
): List<T> {
	return if (isAscending) {
		this.sortedBy(propertySelector)
	} else {
		this.sortedByDescending(propertySelector)
	}
}