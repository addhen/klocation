package com.addhen.klocation.sample.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldSample(title: String, content: @Composable BoxScope.() -> Unit) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = title) },
        navigationIcon = { },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent,
          scrolledContainerColor = Color.Transparent,
        ),
        modifier = Modifier.fillMaxWidth(),
      )
    },
    modifier = Modifier.fillMaxSize()
  ) { contentPadding ->

    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(contentPadding)
        .consumeWindowInsets(contentPadding)
        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)),
      content = content
    )
  }
}
