package dev.omkartenkale.explodable

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import dev.omkartenkale.explodable.ExplosionAnimationSpec.Companion.SHRINK_DURATION_MS
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun Explodable(
    modifier: Modifier = Modifier,
    controller: ExplosionController,
    animationSpec: ExplosionAnimationSpec = ExplosionAnimationSpec(),
    currentProgress: Float? = null,
    onExplode: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val animationScope = rememberCoroutineScope()
    var explosionViewBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var explosionBound by remember { mutableStateOf(Rect.Zero) }

    val animatedProgress = remember { Animatable(0f) }
    val unitProgress = currentProgress ?: animatedProgress.value

    val explosionPower = animationSpec.explosionPower
    val shakeDurationMs = animationSpec.shakeDurationMs.toFloat()
    val explosionDurationMs = animationSpec.explosionDurationMs.toFloat()
    val animationDurationMs = shakeDurationMs + explosionDurationMs
    val currentPositionMs = animationDurationMs * unitProgress

    Box(modifier = modifier) {
        if (currentPositionMs > shakeDurationMs) {
            explosionViewBitmap?.let {
                Explosion(
                    progress = currentPositionMs.mapInRange(
                        shakeDurationMs,
                        animationDurationMs,
                        0f,
                        1f
                    ), bitmap = it, bound = explosionBound
                )
            }
        }

        val captureController = rememberCaptureController()
        Capturable(controller = captureController, onCaptured = { bitmap, error ->
            bitmap?.let {
                explosionViewBitmap = it
            }
            error?.let {
                Log.e("ExplodingComposable", "Failed to extract colors from content")
            }
        }) {
            val contentScaleAndAlpha = when {
                currentPositionMs < shakeDurationMs -> 1f
                currentPositionMs > (shakeDurationMs + SHRINK_DURATION_MS) -> 0f
                else -> (currentPositionMs - shakeDurationMs).mapInRange(
                    0f,
                    SHRINK_DURATION_MS.toFloat(),
                    1f,
                    0f
                )
            }
            var contentSize by remember { mutableStateOf(IntSize.Zero) }
            Box(modifier = Modifier.wrapContentSize().onGloballyPositioned { coordinates ->
                contentSize = coordinates.size
                explosionBound = coordinates.boundsInWindow().mutate {
                    with(coordinates.positionInWindow()) {
                        offset(-x, -y)
                    }
                    scale(explosionPower)
                }.toRect()
                if (explosionViewBitmap == null) {
                    captureController.capture()
                }
            }.scale(contentScaleAndAlpha).alpha(contentScaleAndAlpha).let {
                if (currentPositionMs > 0 && currentPositionMs < shakeDurationMs) {
                    with(remember { Random() }) {
                        it.offset(
                            x = Dp((nextFloat() - 0.5f) * contentSize.width * 0.05f),
                            y = Dp((nextFloat() - 0.5f) * contentSize.height * 0.05f)
                        )
                    }
                } else it
            }) { content() }
        }
    }

    LaunchedEffect(animationSpec) {
        controller.explosionRequests.onEach {
            when (it) {
                ExplosionRequest.EXPLODE -> {
                    animationScope.launch {
                        animatedProgress.snapTo(0f)
                        animatedProgress.animateTo(targetValue = 1f, animationSpec = keyframes {
                            durationMillis = animationDurationMs.toInt()
                            0.0f at 0
                            (shakeDurationMs / animationDurationMs) at shakeDurationMs.toInt() with LinearEasing
                            1f at animationDurationMs.toInt() with CubicBezierEasing(
                                0.32f,
                                0f,
                                0.67f,
                                0f
                            )
                        }) {
                            if (value == 1f) {
                                onExplode()
                            }
                        }
                    }
                }
                ExplosionRequest.RESET -> {
                    animatedProgress.snapTo(0f)
                }
            }
        }.launchIn(this)
    }
}