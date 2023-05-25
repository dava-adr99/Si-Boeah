package com.capstonebangkit.siboeah

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstonebangkit.siboeah.ui.theme.SiboeahTheme

class PemindaiBuahActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SiboeahTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { navigateToCameraActivity() },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "Buka CameraX Pemindai Buah")
                        }
                    }
                }
            }
        }
    }

    private fun navigateToCameraActivity() {
        val intent = Intent(this, CameraX_PemindaiBuahActivity::class.java)
        startActivity(intent)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SiboeahTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {  },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Buka CameraX Pemindai Buah")
                }
            }
        }
    }
}