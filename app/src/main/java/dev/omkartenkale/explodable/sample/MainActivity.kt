package dev.omkartenkale.explodable.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dev.omkartenkale.explodable.sample.ui.theme.ExplodingComposableTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExplodingComposableTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFf5f6f8)
                ) {
                    ScreenContent()
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScreenContent() {
    HorizontalPager(count = 5, state = rememberPagerState()) { pageIndex ->
        when (pageIndex) {
            0 -> AppUninstallScreen()
            1 -> DeleteFolderScreen()
            2 -> RemoveBookmarkScreen()
            3 -> ManualAnimationControlScreen()
            4 -> AnimationCustomizationDemoScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ExplodingComposableTheme {
        ScreenContent()
    }
}