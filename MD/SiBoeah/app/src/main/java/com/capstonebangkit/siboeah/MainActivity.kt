package com.capstonebangkit.siboeah


import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import com.capstonebangkit.siboeah.ui.theme.HijauMuda
import com.capstonebangkit.siboeah.ui.theme.HijauTua
import com.capstonebangkit.siboeah.ui.theme.Krem
import com.capstonebangkit.siboeah.ui.theme.Kuning

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
    val showFullScreen = remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        delay(2000) // Delay for 2 seconds
        showFullScreen.value = false
        delay(1000) // Additional delay for full-screen background visibility (adjust as needed)
        onSplashComplete()
    }

    Box(
        modifier = if (showFullScreen.value) Modifier.fillMaxSize()

            else Modifier .background(color = HijauTua),
        contentAlignment = Alignment.Center,

    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(
                    color = if (showFullScreen.value) HijauTua else HijauTua,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo_splash_screen), // Sumber icon atau Logo
                contentDescription = "Logo",
                tint = Krem, // Icon berwarna  Krem
                modifier = Modifier.size(120.dp)
            )
        }
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
