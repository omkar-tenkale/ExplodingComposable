package dev.omkartenkale.explodable

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.omkartenkale.explodable.Particle.Particle.END_VALUE
import dev.omkartenkale.explodable.Particle.Particle.V
import java.util.*

@Composable
internal fun Explosion(progress: Float, bitmap: ImageBitmap, bound: Rect) {
    val particles = remember {
        mutableListOf<Particle>().apply {
            val partLen = 15
            val w = bitmap.width / (partLen + 2)
            val h = bitmap.height / (partLen + 2)
            for (i in 0 until partLen) {
                for (j in 0 until partLen) {
                    add(Particle.Particle(bitmap.getPixel((j + 1) * w, (i + 1) * h), bound))
                }
            }
        }
    }

    val mappedProgress = progress.mapInRange(0f, 1f, 0f, END_VALUE)
    Canvas(modifier = Modifier, onDraw = {
        particles.forEach {
            it.updateProgress(mappedProgress, V)
            if (it.alpha > 0f) {
                drawCircle(
                    center = Offset(it.cx, it.cy),
                    radius = it.radius,
                    alpha = it.color.alpha * it.alpha,
                    color = it.color
                )
            }
        }
    })
}

private fun DrawScope.drawDebugRect(bound: Rect) {
    drawRect(
        color = Color.Red,
        topLeft = Offset(bound.left, bound.top),
        size = Size(bound.right - bound.left, bound.bottom - bound.top)
    )
}

private class Particle {
    var alpha = 0f
    var color = Color.Transparent
    var cx = 0f
    var cy = 0f
    var radius = 0f
    var baseCx = 0f
    var baseCy = 0f
    var baseRadius = 0f
    var top = 0f
    var bottom = 0f
    var mag = 0f
    var neg = 0f
    var life = 0f
    var overflow = 0f
    fun updateProgress(factor: Float, V: Float) {
        var f = 0f
        var normalization = factor / END_VALUE
        if (normalization < life || normalization > 1f - overflow) {
            alpha = 0f
            return
        }
        normalization = (normalization - life) / (1f - life - overflow)
        val f2 = normalization * END_VALUE
        if (normalization >= 0.7f) {
            f = (normalization - 0.7f) / 0.3f
        }
        alpha = 1f - f
        f = bottom * f2
        cx = baseCx + f
        cy = (baseCy - neg * Math.pow(f.toDouble(), 2.0)).toFloat() - f * mag
        radius = V + (baseRadius - V) * f2
    }

    companion object Particle {
        const val END_VALUE = 1.4f
        val X = 5.dpToPx()
        val Y = 20.dpToPx()
        val V = 2.dpToPx()
        val W = 1.dpToPx()
        val random = Random(System.currentTimeMillis())

        fun Particle(color: Int, bound: Rect) = Particle().apply {
            this.color = Color(color)
            radius = V
            if (random.nextFloat() < 0.2f) {
                baseRadius = V + (X - V) * random.nextFloat()
            } else {
                baseRadius = W + (V - W) * random.nextFloat()
            }
            val nextFloat = random.nextFloat()
            top = bound.height * (0.18f * random.nextFloat() + 0.2f)
            top = if (nextFloat < 0.2f) top else top + top * 0.2f * random.nextFloat()
            bottom = bound.height * (random.nextFloat() - 0.5f) * 1.8f
            var f =
                if (nextFloat < 0.2f) bottom else if (nextFloat < 0.8f) bottom * 0.6f else bottom * 0.3f
            bottom = f
            mag = 4.0f * top / bottom
            neg = -mag / bottom
            f = bound.centerX + Y * (random.nextFloat() - 0.5f)
            baseCx = f
            cx = f
            f = bound.centerY + Y * (random.nextFloat() - 0.5f)
            baseCy = f
            cy = f
            life = END_VALUE / 10 * random.nextFloat()
            overflow = 0.4f * random.nextFloat()
            alpha = 1f
        }
    }
}