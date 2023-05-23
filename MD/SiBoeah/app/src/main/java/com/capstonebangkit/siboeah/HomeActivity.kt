package com.capstonebangkit.siboeah



import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp


class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen()
        }
    }
}

@Composable
fun HomeScreen() {
    val images = listOf(
        R.drawable.banner1, // Gambar untuk slide pertama
        R.drawable.banner2 // Gambar untuk slide kedua
    )
    val context = LocalContext.current
    var currentPage by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()


    ) {
        Spacer(Modifier.weight(1f))
        BannerCard(images, currentPage, coroutineScope, context) { page ->
            currentPage = page
        }
        Spacer(Modifier.weight(1f))
        FeatureMenuCard()
        Spacer(Modifier.weight(1f))
    }
}


@Composable
fun BannerCard(
    images: List<Int>,
    currentPage: Int,
    coroutineScope: CoroutineScope,
    context: Context,
    onPageChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.Blue)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(images[currentPage]),
                contentDescription = "Banner Image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalPagerIndicator(
                count = images.size,
                activeIndex = currentPage, // Adjusted parameter name
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                activeColor = Color.White,
                inactiveColor = Color.Gray
            )
        }
    }

    // Auto-scroll to the next slide every 3 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            coroutineScope.launch {
                onPageChange((currentPage + 1) % images.size)
            }
        }
    }
}

@Composable
fun HorizontalPagerIndicator(
    count: Int,
    activeIndex: Int,
    activeColor: Color = Color.White,
    inactiveColor: Color = Color.Gray,
    modifier: Modifier = Modifier,
    indicatorSize: Dp = 8.dp,
    spacing: Dp = 4.dp
) {
    Row(modifier = modifier) {
        for (i in 0 until count) {
            val color = if (i == activeIndex) activeColor else inactiveColor
            Box(
                modifier = Modifier
                    .size(indicatorSize)
                    .background(color = color)
            )
            if (i < count - 1) {
                Spacer(modifier = Modifier.width(spacing))
            }
        }
    }
}
@Composable
fun FeatureMenuCard() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(Color.Green)
    ) {
        Text(
            text = "Feature and Menu Card",
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
