package com.capstonebangkit.siboeah


import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SiBoeahTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    SplashScreen(onSplashComplete = { navigateToWelcomeScreen() })
                }
            }
        }
    }

    private fun navigateToWelcomeScreen() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    // Simulate a delay for the splash screen
    LaunchedEffect(key1 = true) {
        delay(2000) // Adjust the delay time as needed
        onSplashComplete()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Splash Screen",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SiBoeahTheme {
        SplashScreen(onSplashComplete = {})
    }
}

@Composable
fun SiBoeahTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}
