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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.addhen.klocation.LocationState
import com.addhen.klocation.sample.shared.Samples
import kotlinx.coroutines.launch

@Composable
fun LocationScreen(locationViewModel: LocationViewModel) {
  val uiState by locationViewModel.viewState.collectAsState()
  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(Unit) {
    coroutineScope.launch { locationViewModel.getLastKnowLocation() }
  }

  when (uiState.flag) {
    LocationViewModel.LocationUiState.Flag.LOADING -> FullScreenLoading()
    LocationViewModel.LocationUiState.Flag.ERROR -> Unit
    LocationViewModel.LocationUiState.Flag.IDLE -> {
      val currentLocation = when (uiState.observeLocationState) {
        is LocationState.LocationDisabled,
        is LocationState.Error,
        LocationState.NoNetworkEnabled,
        LocationState.PermissionMissing,
        -> {
          ""
        }

        is LocationState.CurrentLocation<*> -> {
          val observeLocationState = uiState.observeLocationState
          val location =
            ((observeLocationState as LocationState.CurrentLocation<*>).location as? Location)
          "${location?.latitude},${location?.longitude}"
        }
      }

      val lastKnownLocation = when (uiState.lastKnowLocationState) {
        is LocationState.LocationDisabled,
        is LocationState.Error,
        LocationState.NoNetworkEnabled,
        LocationState.PermissionMissing,
        -> {
          ""
        }

        is LocationState.CurrentLocation<*> -> {
          val lastKnowLocation = uiState.lastKnowLocationState
          val location =
            ((lastKnowLocation as LocationState.CurrentLocation<*>).location as? Location)
          "${location?.latitude},${location?.longitude}"
        }
      }

      Samples(
        currentLocation = currentLocation,
        lastKnownLocation = lastKnownLocation,
      ) {
        locationViewModel.stopLocating()
      }
    }
  }
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
