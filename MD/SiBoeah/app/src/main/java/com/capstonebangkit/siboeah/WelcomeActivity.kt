package com.capstonebangkit.siboeah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstonebangkit.siboeah.ui.theme.HijauMuda
import com.capstonebangkit.siboeah.ui.theme.HijauTua
import com.capstonebangkit.siboeah.ui.theme.Kuning


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
        WelcomeSlide(R.drawable.page1_logo_master, "Slide 1", HijauTua), // Warna Hijau tua
        WelcomeSlide(R.drawable.page2_illustration, "Slide 2", Kuning), // Warna Kuning
        WelcomeSlide(R.drawable.page3_illustration, "Slide 3", HijauMuda) // Warna Hijau muda
    )

    val buttonColor = slides[currentSlide.value].buttonColor

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
            style = typography.headlineMedium
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
            modifier = Modifier.wrapContentWidth(),
            colors = ButtonDefaults.buttonColors(
                buttonColor,
                contentColor = Color.White
            ),
            content = {
                if (currentSlide.value < slides.size - 1) {
                    Text(text = "Next")
                } else {
                    Text(text = "Mulai")
                }
            }
        )
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