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
public fun locationUpdatesState(locationService: LocationService): State<LocationState> {
  return locationService
    .requestLocationUpdates()
    .distinctUntilChanged()
    .collectAsState(LocationState.CurrentLocation(null))
}

@Composable
public fun lastKnowLocationState(locationService: LocationService): State<LocationState> {
  return produceState<LocationState>(LocationState.CurrentLocation(null)) {
    value = locationService.getLastKnownLocation()
  }
}

@Composable
public fun lastKnowLocationState(
  locationService: LocationService,
  key1: Any?,
): State<LocationState> {
  return produceState<LocationState>(
    LocationState.CurrentLocation(null),
    key1,
  ) {
    value = locationService.getLastKnownLocation()
  }
}

@Composable
public fun lastKnowLocationState(
  locationService: LocationService,
  key1: Any?,
  key2: Any?,
): State<LocationState> {
  return produceState<LocationState>(
    LocationState.CurrentLocation(null),
    key1,
    key2,
  ) {
    value = locationService.getLastKnownLocation()
  }
}

@Composable
public fun lastKnowLocationState(
  locationService: LocationService,
  key1: Any?,
  key2: Any?,
  key3: Any?,
): State<LocationState> {
  return produceState<LocationState>(
    LocationState.CurrentLocation(null),
    key1,
    key2,
    key3,
  ) {
    value = locationService.getLastKnownLocation()
  }
}

@Composable
public fun lastKnowLocationState(
  locationService: LocationService,
  vararg keys: Any?,
): State<LocationState> {
  return produceState<LocationState>(LocationState.CurrentLocation(null), keys) {
    value = locationService.getLastKnownLocation()
  }
}
