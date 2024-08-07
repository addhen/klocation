// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import com.addhen.klocation.LocationService
import com.addhen.klocation.LocationState
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun locationUpdatesState(locationService: LocationService): State<LocationState> {
  return locationService
    .observeLocationUpdates()
    .distinctUntilChanged()
    .collectAsState(LocationState.CurrentLocation(null))
}

@Composable
fun lastKnowLocationState(locationService: LocationService): State<LocationState> {
  return produceState(LocationState.CurrentLocation(null)) {
    locationService.getLastKnownLocation()
  }
}
