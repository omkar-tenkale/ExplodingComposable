package dev.omkartenkale.explodable.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.omkartenkale.explodable.Explodable
import dev.omkartenkale.explodable.ExplosionAnimationSpec
import dev.omkartenkale.explodable.rememberExplosionController

@Preview
@Composable
fun AnimationCustomizationDemoScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.size(100.dp))

            var explosionDurationMs by remember { mutableStateOf(750f) }
            var shakeDurationMs by remember { mutableStateOf(150f) }
            var contentSizeDp by remember { mutableStateOf(90f) }
            var explosionPower by remember { mutableStateOf(2f) }
            val animationSpec = ExplosionAnimationSpec(
                shakeDurationMs = shakeDurationMs.toInt(),
                explosionPower = explosionPower,
                explosionDurationMs = explosionDurationMs.toInt()
            )

            val explosionController = rememberExplosionController()
            Explodable(
                modifier = Modifier.wrapContentSize(),
                controller = explosionController,
                animationSpec = animationSpec,
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .size(contentSizeDp.dp)
                ) {
                    Image(
                        painterResource(
                            id = remember {
                                listOf(
                                    R.drawable.instagram,
                                    R.drawable.google_maps,
                                    R.drawable.firefox
                                ).random()
                            }
                        ), contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Size ${"%.1f".format(contentSizeDp)}dp",
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = contentSizeDp,
                onValueChange = { contentSizeDp = it },
                valueRange = 20f..300f
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Shake Duration ${"%.1f".format(shakeDurationMs / 1000)}s",
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = shakeDurationMs,
                onValueChange = { shakeDurationMs = it },
                valueRange = 0f..7500f
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Explosion Duration ${"%.1f".format(explosionDurationMs / 1000)}s",
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = explosionDurationMs,
                onValueChange = { explosionDurationMs = it },
                valueRange = 0f..7500f
            )


            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Explosion power ${"%.1f".format(explosionPower)}",
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = explosionPower,
                onValueChange = { explosionPower = it },
                valueRange = 0.1f..15f
            )

            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.ic_refresh),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                            explosionController.reset()
                        })
                Spacer(modifier = Modifier.width(48.dp))
                Image(painter = painterResource(id = R.drawable.ic_explode),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            explosionController.explode()
                        })
            }
        }
    }
}