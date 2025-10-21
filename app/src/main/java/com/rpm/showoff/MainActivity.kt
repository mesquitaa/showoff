package com.rpm.showoff

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rpm.core.ui.theme.ShowOffTheme
import com.rpm.showoff.navigation.MealNavigation

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ShowOffTheme {
        MealNavigation(onYouTubeLinkClicked = ::watchYoutubeVideo)
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
