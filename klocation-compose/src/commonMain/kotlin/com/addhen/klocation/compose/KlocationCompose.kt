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

/**
 * Provides a [State] object that emits location updates.
 *
 * This function uses [collectAsState] to convert the Flow of location updates into a [State] object.
 * It applies [distinctUntilChanged] to ensure that only distinct location updates are emitted.
 *
 * @return A [State] that represents the current location state.
 *         The initial value is [LocationState.CurrentLocation] with a null location.
 */
@Composable
public fun locationUpdatesState(locationService: LocationService): State<LocationState> =
  locationService
    .requestLocationUpdates()
    .distinctUntilChanged()
    .collectAsState(LocationState.CurrentLocation(null))

/**
 * Provides a [State] object that contains the last known location.
 *
 * This function uses [produceState] to create a [State] object that contains the last known location.
 * The location is retrieved asynchronously using [LocationService.getLastKnownLocation].
 *
 * @return A [State] that represents the last known location.
 *         The initial value is [LocationState.CurrentLocation] with a null location.
 */
@Composable
public fun lastKnowLocationState(locationService: LocationService): State<LocationState> =
  produceState<LocationState>(LocationState.CurrentLocation(null)) {
    value = locationService.getLastKnownLocation()
  }

/**
 * Provides a [State] object that contains the last known location, with one recomposition key.
 *
 * @param key1 A key that can be used to force recomposition when it changes.
 * @return A [State] that represents the last known location.
 *         The initial value is [LocationState.CurrentLocation] with a null location.
 */
@Composable
public fun lastKnowLocationState(
  locationService: LocationService,
  key1: Any?,
): State<LocationState> = produceState<LocationState>(
  LocationState.CurrentLocation(null),
  key1,
) {
  value = locationService.getLastKnownLocation()
}

/**
 * Provides a [State] object that contains the last known location, with two recomposition keys.
 *
 * @param key1 First key that can be used to force recomposition when it changes.
 * @param key2 Second key that can be used to force recomposition when it changes.
 * @return A [State] that represents the last known location.
 *         The initial value is [LocationState.CurrentLocation] with a null location.
 */
@Composable
public fun lastKnowLocationState(
  locationService: LocationService,
  key1: Any?,
  key2: Any?,
): State<LocationState> = produceState<LocationState>(
  LocationState.CurrentLocation(null),
  key1,
  key2,
) {
  value = locationService.getLastKnownLocation()
}

/**
 * Provides a [State] object that contains the last known location, with three recomposition keys.
 *
 * @param key1 First key that can be used to force recomposition when it changes.
 * @param key2 Second key that can be used to force recomposition when it changes.
 * @param key3 Third key that can be used to force recomposition when it changes.
 * @return A [State] that represents the last known location.
 *         The initial value is [LocationState.CurrentLocation] with a null location.
 */
@Composable
public fun lastKnowLocationState(
  locationService: LocationService,
  key1: Any?,
  key2: Any?,
  key3: Any?,
): State<LocationState> = produceState<LocationState>(
  LocationState.CurrentLocation(null),
  key1,
  key2,
  key3,
) {
  value = locationService.getLastKnownLocation()
}

/**
 * Provides a [State] object that contains the last known location, with a variable number
 * of recomposition keys.
 *
 * @param keys A variable number of keys that can be used to force recomposition when any
 *             of them change.
 * @return A [State] that represents the last known location.
 *         The initial value is [LocationState.CurrentLocation] with a null location.
 */
@Composable
public fun lastKnowLocationState(
  locationService: LocationService,
  vararg keys: Any?,
): State<LocationState> = produceState<LocationState>(LocationState.CurrentLocation(null), keys) {
  value = locationService.getLastKnownLocation()
}
