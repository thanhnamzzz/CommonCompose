package common.libs.compose.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/**
 * Returns a contrasting color (either [Color.Black] or [Color.White]) based on the
 * perceived luminance of this color. This is useful for ensuring text or icons
 * are visible against a given background color.
 */
fun Color.contrastingColor(): Color {
    return if (luminance() > 0.5f) Color.Black else Color.White
}
