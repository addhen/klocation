// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import kotlinx.coroutines.flow.Flow
import platform.CoreLocation.CLLocation

/**
 * This class provides methods to request for location updates, retrieve the last known location,
 * and stop location tracking. It uses a [LocationProvider] to handle the actual location operations.
 *
 * @param locationProvider The underlying [LocationProvider] used for location operations.
 *                         Defaults to CLLocationProvider.
 */
public actual class LocationService(
  public actual val locationProvider: LocationProvider = CLLocationProvider(),
) {

  /**
   * Requests location updates from the underlying location provider.
   *
   * @return A [Flow] of [LocationState] representing the stream of location updates.
   */
  public actual fun requestLocationUpdates(): Flow<LocationState> {
    return locationProvider.requestLocationUpdates()
  }

  /**
   * Retrieves the last known location from the underlying location provider.
   *
   * @return A [LocationState] representing the last known location or or other states
   */
  public actual suspend fun getLastKnownLocation(): LocationState {
    return locationProvider.getLastKnownLocation()
  }

  /**
   * Stops requesting location updates from the underlying location provider.
   */
  public actual fun stopRequestingLocationUpdates(): Unit =
    locationProvider.stopRequestingLocationUpdates()
}

/**
 * Converts a [LocationState.CurrentLocation.libLocation] to a [CLLocation]
 * if the location is a [CLLocation].
 *
 * @return A [CLLocation] if the location is a [CLLocation], otherwise null.
 */
public val LocationState.CurrentLocation<*>.cllocation: CLLocation?
  get() = this.libLocation as? CLLocation
