// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation.sample.android

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.addhen.klocation.LocationService
import com.addhen.klocation.LocationState
import com.addhen.klocation.compose.lastKnowLocationState
import com.addhen.klocation.compose.locationUpdatesState
import com.addhen.klocation.sample.shared.Samples
import kotlinx.coroutines.launch

@Composable
fun LocationScreen(
  locationViewModel: LocationViewModel,
  locationService: LocationService
) {
  // Showing how to consume location updates and last known location without compose [State]
  val uiState by locationViewModel.viewState.collectAsState()
  val coroutineScope = rememberCoroutineScope()

  // Showing how to consume the location updates and last known location with compose [State]
  val locationUpdatesState by locationUpdatesState(locationService)
  val lastKnowLocationState by lastKnowLocationState(locationService)

  LaunchedEffect(Unit) {
    coroutineScope.launch { locationViewModel.getLastKnowLocation() }
  }

  when (uiState.flag) {
    LocationViewModel.LocationUiState.Flag.LOADING -> FullScreenLoading()
    LocationViewModel.LocationUiState.Flag.ERROR -> Unit
    LocationViewModel.LocationUiState.Flag.IDLE -> {
      val currentLocation = getLocation(uiState.observeLocationState)
      val lastKnownLocation = getLocation(uiState.lastKnowLocationState)
      val klocationComposeLocationUpdates = getLocation(locationUpdatesState)
      val klocationComposeLastKnownLocation = getLocation(lastKnowLocationState)

      Samples(
        currentLocation = currentLocation,
        lastKnownLocation = lastKnownLocation,
        klocationComposeLocationUpdates = klocationComposeLocationUpdates,
        klocationComposeLastKnownLocation = klocationComposeLastKnownLocation,
      ) {
        locationViewModel.stopLocating()
      }
    }
  }
}

@Composable
private fun getLocation(locationState: LocationState): String {
  val currentLocation = when (locationState) {
    is LocationState.LocationDisabled,
    is LocationState.Error,
    LocationState.NoNetworkEnabled,
    LocationState.PermissionMissing,
    -> {
      ""
    }

    is LocationState.CurrentLocation<*> -> {
      val location = locationState.location as? Location
      "${location?.latitude},${location?.longitude}"
    }
  }
  return currentLocation
}

@Composable
private fun FullScreenLoading() {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .wrapContentSize(Alignment.Center),
  ) {
    CircularProgressIndicator()
  }
}
