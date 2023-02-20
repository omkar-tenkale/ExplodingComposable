package dev.omkartenkale.explodable.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.omkartenkale.explodable.Explodable
import dev.omkartenkale.explodable.rememberExplosionController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun AppUninstallScreen() {
    val coroutineScope = rememberCoroutineScope()
    var appOptionsVisible by remember { mutableStateOf(false) }
    val explosionController = rememberExplosionController()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier
                .width(300.dp)
                .clip(RoundedCornerShape(12.dp)),
            painter = painterResource(id = R.drawable.launcher_background),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Box {
            Explodable(controller = explosionController, onExplode = {
                coroutineScope.launch {
                    delay(2000)
                    explosionController.reset()
                }
            }) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon_mi_security),
                    contentDescription = null,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            appOptionsVisible = true
                        })
            }

            DropdownMenu(
                expanded = appOptionsVisible,
                onDismissRequest = {}
            ) {
                DropdownMenuItem(onClick = {
                    appOptionsVisible = false
                    explosionController.explode()
                }) {
                    Text("Uninstall")
                }
            }
        }
    }
}