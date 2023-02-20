package dev.omkartenkale.explodable

import android.content.res.Resources
import android.util.Log
import androidx.compose.ui.geometry.MutableRect
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap

fun ImageBitmap.getPixel(x: Int, y: Int) = IntArray(1).apply {
    readPixels(this, startX = x, startY = y, width = 1, height = 1)
}.first()

fun Float.mapInRange(inMin: Float, inMax: Float, outMin: Float, outMax: Float): Float {
    return outMin + (((this - inMin) / (inMax - inMin)) * (outMax - outMin))
}

fun Int.dpToPx() = toFloat().dpToPx()

fun Float.dpToPx() = this * Resources.getSystem().displayMetrics.density

val Rect.centerX get() = left + (width / 2)
val Rect.centerY get() = top + (height / 2)

internal fun LOG(message: String) = Log.e("ExplodingComposable", message)

fun Rect.mutate(block: MutableRect.() -> Unit) = MutableRect(left, top, right, bottom).apply {
    block(this)
}

fun MutableRect.offset(dx: Float, dy: Float) {
    left += dx
    top += dy
    right += dx
    bottom += dy
}

fun MutableRect.scale(factor: Float) {
    val oldWidth = width
    val oldHeight = height
    val rectCenterX = left + oldWidth / 2F
    val rectCenterY = top + oldHeight / 2F
    val newWidth = oldWidth * factor
    val newHeight = oldHeight * factor
    left = rectCenterX - newWidth / 2F
    right = rectCenterX + newWidth / 2F
    top = rectCenterY - newHeight / 2F
    bottom = rectCenterY + newHeight / 2F
}