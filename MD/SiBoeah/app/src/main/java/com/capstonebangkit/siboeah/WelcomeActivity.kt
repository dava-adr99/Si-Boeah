package com.capstonebangkit.siboeah

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstonebangkit.siboeah.ui.theme.SiBoeahTheme

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SiBoeahTheme {
                WelcomeScreen(onStartClick = { navigateToHomeScreen() })
            }
        }
    }

    private fun navigateToHomeScreen() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

}

@Composable
fun WelcomeScreen(onStartClick: () -> Unit) {
    val currentSlide = remember { mutableStateOf(0) }

    val slides = listOf(
        WelcomeSlide(R.drawable.image1, "Slide 1", Color(0xFF006400)), // Hijau tua
        WelcomeSlide(R.drawable.image2, "Slide 2", Color.Yellow), // Kuning
        WelcomeSlide(R.drawable.image3, "Slide 3", Color(0xFF98FB98)) // Hijau muda
    )

    val currentSlideColor = slides[currentSlide.value].buttonColor

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = slides[currentSlide.value].imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = slides[currentSlide.value].text,
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (currentSlide.value < slides.size - 1) {
                    currentSlide.value++
                } else {
                    onStartClick()
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = currentSlideColor)
        ) {
            if (currentSlide.value < slides.size - 1) {
                Text(text = "Next")
            } else {
                Text(text = "Mulai")
            }
        }
    }
}

data class WelcomeSlide(val imageRes: Int, val text: String, val buttonColor: Color)

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    SiBoeahTheme {
        WelcomeScreen(onStartClick = {})
    }
}