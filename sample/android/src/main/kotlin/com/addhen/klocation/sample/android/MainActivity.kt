package com.addhen.klocation.sample.android

import android.os.Bundle
import androidx.core.app.ComponentActivity
import com.addhen.klocation.sample.shared.Samples

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)

    setContent {
      Samples(appTitle = title.toString())
    }
  }
}
