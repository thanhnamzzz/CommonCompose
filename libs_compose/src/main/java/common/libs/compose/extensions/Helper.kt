package common.libs.compose.extensions

import android.os.Handler
import android.os.Looper

fun handlerFunction(timeWait: Long, callback: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({ callback() }, timeWait)
}