package com.rpm.showoff

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.rpm.core.ui.theme.ShowOffTheme
import com.rpm.showoff.navigation.MealNavigation

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ShowOffTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          Surface(
            modifier = Modifier.padding(innerPadding),
            color = MaterialTheme.colorScheme.background,
          ) {
            MealNavigation(onYouTubeLinkClicked = ::watchYoutubeVideo)
          }
        }
      }
    }
  }

  private fun watchYoutubeVideo(url: String) {
    val webIntent = Intent(
      Intent.ACTION_VIEW,
      Uri.parse(url),
    )
    startActivity(webIntent)
  }
}
