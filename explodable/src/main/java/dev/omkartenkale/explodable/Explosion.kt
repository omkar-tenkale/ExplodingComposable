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
import androidx.compose.ui.unit.dp
import kotlin.math.pow

@Composable
internal fun Explosion(progress: Float, bitmap: ImageBitmap, bound: Rect) {
    val particles = remember {
        mutableListOf<Particle>().apply {
            val partLen = 15
            val w = bitmap.width / (partLen + 2)
            val h = bitmap.height / (partLen + 2)
            for (i in 0 until partLen) {
                for (j in 0 until partLen) {
                    add(
                        Particle(
                            color = Color(bitmap.getPixel((j + 1) * w, (i + 1) * h)),
                            startXPosition = bound.centerX.toInt(),
                            startYPosition = bound.centerY.toInt(),
                            maxVerticalDisplacement = bound.height * randomInRange(0.2f, 0.38f),
                            maxHorizontalDisplacement = bound.width * randomInRange(-0.9f, 0.9f)
                        )
                    )
                }
            }
        }
    }

    Canvas(modifier = Modifier, onDraw = {
        particles.forEach {
            it.updateProgress(progress)
            if (it.alpha > 0f) {
                drawCircle(
                    center = Offset(it.currentXPosition, it.currentYPosition),
                    radius = it.currentRadius,
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

class Particle(
    val color: Color,
    val startXPosition: Int,
    val startYPosition: Int,
    val maxHorizontalDisplacement: Float,
    val maxVerticalDisplacement: Float
) {
    val velocity = 4 * maxVerticalDisplacement
    val acceleration = -2 * velocity
    var currentXPosition = 0f
    var currentYPosition = 0f

    var visibilityThresholdLow = randomInRange(0f, 0.14f)
    var visibilityThresholdHigh = randomInRange(0f, 0.4f)

    val initialXDisplacement = 10.dp.toPx() * randomInRange(-1f, 1f)
    val initialYDisplacement = 10.dp.toPx() * randomInRange(-1f, 1f)

    var alpha = 0f
    var currentRadius = 0f
    val startRadius = 2.dp.toPx()
    val endRadius = if (randomBoolean(trueProbabilityPercentage = 20)) {
        randomInRange(startRadius, 5.dp.toPx())
    } else {
        randomInRange(1.5.dp.toPx(), startRadius)
    }

    fun updateProgress(explosionProgress: Float) {
        val trajectoryProgress =
            if (explosionProgress < visibilityThresholdLow || (explosionProgress > (1 - visibilityThresholdHigh))) {
                alpha = 0f; return
            } else (explosionProgress - visibilityThresholdLow).mapInRange(
                0f, 1f - visibilityThresholdHigh - visibilityThresholdLow, 0f, 1f
            )
        alpha = if (trajectoryProgress < 0.7f) 1f else (trajectoryProgress - 0.7f).mapInRange(
            0f, 0.3f, 1f, 0f
        )
        currentRadius = startRadius + (endRadius - startRadius) * trajectoryProgress
        val currentTime = trajectoryProgress.mapInRange(0f, 1f, 0f, 1.4f)
        val verticalDisplacement =
            (currentTime * velocity + 0.5 * acceleration * currentTime.toDouble()
                .pow(2.0)).toFloat()
        currentYPosition = startXPosition + initialXDisplacement - verticalDisplacement
        currentXPosition =
            startYPosition + initialYDisplacement + maxHorizontalDisplacement * trajectoryProgress
    }
}