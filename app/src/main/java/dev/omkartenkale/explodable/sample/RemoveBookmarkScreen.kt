package dev.omkartenkale.explodable.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import dev.omkartenkale.explodable.Explodable
import dev.omkartenkale.explodable.rememberExplosionController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun RemoveBookmarkScreen() {
    val coroutineScope = rememberCoroutineScope()
    var folderOptionsVisible by remember { mutableStateOf(false) }
    val explosionController = rememberExplosionController()

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (background, folder) = createRefs()

        Image(modifier = Modifier
            .width(300.dp)
            .clip(RoundedCornerShape(12.dp))
            .constrainAs(background) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
            .background(Color.Red),
            painter = painterResource(id = R.drawable.browser_background),
            contentDescription = null,
            contentScale = ContentScale.FillWidth)

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .padding(top = 42.dp)
            .constrainAs(folder) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {
            Explodable(controller = explosionController, onExplode = {
                coroutineScope.launch {
                    delay(2000)
                    explosionController.reset()
                }
            }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.twitter),
                        contentDescription = null,
                        modifier = Modifier
                            .size(46.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                folderOptionsVisible = true
                            })
                    Text(
                        text = "Twitter",
                        fontSize = 9.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .alpha(0.8f)
                    )
                }
            }
            DropdownMenu(
                expanded = folderOptionsVisible,
                onDismissRequest = { folderOptionsVisible = false }
            ) {
                DropdownMenuItem(onClick = {
                    folderOptionsVisible = false
                    explosionController.explode()
                }) {
                    Text("Delete")
                }
            }
        }
    }
}