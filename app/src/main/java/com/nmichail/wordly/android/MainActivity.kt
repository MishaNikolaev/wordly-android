package com.nmichail.wordly.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.nmichail.wordly.android.ui.theme.WordlyAndroidTheme

class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			WordlyAndroidTheme {
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
					Text(
						text = "Wordly",
						modifier = Modifier.padding(innerPadding),
					)
				}
			}
		}
	}
}
