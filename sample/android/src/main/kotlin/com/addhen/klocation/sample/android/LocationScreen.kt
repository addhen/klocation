// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation.sample.android

import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.addhen.klocation.LocationState
import com.addhen.klocation.sample.shared.Samples

@Composable
fun LocationScreen(locationViewModel: LocationViewModel) {
  val providers = listOf("Android", "Play Service Fuse")
  var selectedProvider by rememberSaveable { mutableStateOf(0) }
  var trackerTitle by rememberSaveable { mutableStateOf(providers[selectedProvider]) }
  var currentLocation by rememberSaveable { mutableStateOf("") }
  var lastKnownLocation by rememberSaveable { mutableStateOf("") }
  val uiState by locationViewModel.viewState.collectAsState()
  val coroutineScope = rememberCoroutineScope()

  /*LaunchedEffect(lastKnownLocation) {
    coroutineScope.launch {
      when(val locationState = locationService.getLastKnownLocation()) {
        is LocationState.LocationDisabled -> Unit

        is LocationState.CurrentLocation<*> -> {
          val location = (locationState.location as Location)
          currentLocation = "${location.latitude},${location.longitude}"
          println("Location")
        }
        is LocationState.Error -> Unit
        LocationState.NoNetworkEnabled -> Unit
        LocationState.PermissionMissing -> Unit
      }
    }
  }*/

  when (uiState) {
    is LocationState.LocationDisabled -> {
      Samples(
        trackerTitle = "Location Sample",
        currentLocation = currentLocation,
        lastKnownLocation = lastKnownLocation,
        locationProviderList = providers,
        selectedIndex = selectedProvider,
        onItemClick = { index ->
          trackerTitle = providers[index]
        },
      ) {
      }
    }

    is LocationState.CurrentLocation<*> -> {
      val location = ((uiState as LocationState.CurrentLocation<*>).location as? Location)
      currentLocation = "${location?.latitude},${location?.longitude}"
      println("location $location")
      Samples(
        trackerTitle = "Location Sample",
        currentLocation = currentLocation,
        lastKnownLocation = lastKnownLocation,
        locationProviderList = providers,
        selectedIndex = selectedProvider,
        onItemClick = { index ->
          trackerTitle = providers[index]
        },
      ) {
      }
    }
    is LocationState.Error -> Unit
    LocationState.NoNetworkEnabled -> Unit
    LocationState.PermissionMissing -> Unit
  }

  /*Samples(
    trackerTitle = "Location Sample",
    currentLocation = currentLocation,
    lastKnownLocation = lastKnownLocation,
    locationProviderList = providers,
    selectedIndex = selectedProvider,
    onItemClick = { index ->
      trackerTitle = providers[index]
    }) {
  }*/
}
