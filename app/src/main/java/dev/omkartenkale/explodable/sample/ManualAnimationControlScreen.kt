package dev.omkartenkale.explodable.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.omkartenkale.explodable.Explodable
import dev.omkartenkale.explodable.rememberExplosionController

@Preview
@Composable
fun ManualAnimationControlScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .size(300.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(100.dp))
            val explosionController = rememberExplosionController()
            var progress by remember { mutableStateOf(0f) }
            Explodable(
                modifier = Modifier.wrapContentSize(),
                controller = explosionController,
                currentProgress = progress
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .size(90.dp)
                ) {
                    Image(
                        painterResource(
                            id = listOf(
                                R.drawable.instagram,
                                R.drawable.google_maps,
                                R.drawable.firefox
                            ).random()
                        ), contentDescription = null
                    )
                }
            }

            Slider(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                value = progress,
                onValueChange = { progress = it },
                valueRange = 0f..1f
            )
        }
    }
}