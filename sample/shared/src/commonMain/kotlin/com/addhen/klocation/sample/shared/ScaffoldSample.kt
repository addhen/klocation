package com.addhen.klocation.sample.shared

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
fun ScaffoldSample(title: String, content: @Composable (PaddingValues) -> Unit) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = title) },
        navigationIcon = { },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent,
          scrolledContainerColor = Color.Transparent,
        ),
        modifier = Modifier
          .fillMaxWidth(),
      )
    },
    modifier = Modifier.fillMaxSize(),
    content = content
  )
}
