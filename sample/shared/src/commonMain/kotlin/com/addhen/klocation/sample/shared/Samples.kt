// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation.sample.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

@Composable
fun SamplesTheme(useDarkColors: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = if (useDarkColors) darkColorScheme() else lightColorScheme(),
    content = content,
  )
}

@Composable
fun Samples(
  trackerTitle: String,
  currentLocation: String,
  lastKnownLocation: String,
  locationProviderList: List<String>,
  selectedIndex: Int,
  onItemClick: (Int) -> Unit,
  onStopClick: () -> Unit,
) {
  Column(
    modifier = Modifier.padding(16.dp).fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Text("TrackerProvider: $trackerTitle")
    Text("Current Location: $currentLocation")
    Text("Last known location: $lastKnownLocation")
    // DropdownList(locationProviderList, selectedIndex, modifier = Modifier, onItemClick )
    HorizontalDivider(thickness = 2.dp)
    Button(onClick = { onStopClick() }) {
      Text("Stop")
    }
  }
}

// Credits: https://medium.com/@itsuki.enjoy/android-kotlin-jetpack-compose-dropdown-selectable-list-menu-b7ad86ba6a5a
@Composable
fun DropdownList(
  locationProviderList: List<String>,
  selectedIndex: Int,
  modifier: Modifier = Modifier,
  onItemClick: (Int) -> Unit,
) {
  var showDropdown by rememberSaveable { mutableStateOf(true) }
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    // button
    Box(
      modifier = modifier
        .background(Color.Red)
        .clickable { showDropdown = true },
      contentAlignment = Alignment.Center,
    ) {
      Text(text = locationProviderList[selectedIndex], modifier = Modifier.padding(3.dp))
    }

    // dropdown list
    Box {
      if (showDropdown) {
        Popup(
          alignment = Alignment.TopCenter,
          // to dismiss on click outside
          onDismissRequest = { showDropdown = false },
        ) {
          Column(
            modifier = modifier
              .heightIn(max = 90.dp)
              .verticalScroll(state = scrollState)
              .border(width = 1.dp, color = Color.Gray),
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            locationProviderList.onEachIndexed { index, item ->
              if (index != 0) {
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
              }
              Box(
                modifier = Modifier
                  .background(Color.Green)
                  .fillMaxWidth()
                  .clickable {
                    onItemClick(index)
                    showDropdown = !showDropdown
                  },
                contentAlignment = Alignment.Center,
              ) {
                Text(text = item)
              }
            }
          }
        }
      }
    }
  }
}
