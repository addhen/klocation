// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import android.content.Context
import android.location.LocationManager
import kotlinx.coroutines.flow.Flow

/**
 * This class provides methods to request for location updates, retrieve the last known location,
 * and stop location tracking. It uses a [LocationProvider] to handle the actual location operations.
 *
 * @param locationProvider The [LocationProvider] used to manage location updates.
 *                         Defaults to [AndroidLocationProvider].
 */
actual class LocationService(
  actual val locationProvider: LocationProvider,
) {

  /**
   * Constructs a [LocationService] with the given [context] and [locationProvider].
   *
   * @param context The Android [Context] to be used to get the [LocationManager] and for
   *                checking for the necessary required permissions.
   * @param locationProvider The [LocationProvider] used to manage location updates.
   *                          Defaults to [AndroidLocationProvider].
   */
  constructor(
    context: Context,
    locationProvider: LocationProvider = AndroidLocationProvider(context)
  ) : this(locationProvider)

  /**
   * requests location updates as a [Flow] of [LocationState].
   *
   * @return A [Flow] emitting [LocationState] representing location updates or other states.
   */
  actual fun requestLocationUpdates(): Flow<LocationState> {
    return locationProvider.requestLocationUpdates()
  }

  /**
   * Retrieves the last known location.
   *
   * @return A [LocationState] representing the last known location or other states.
   */
  actual suspend fun getLastKnownLocation(): LocationState {
    return locationProvider.getLastKnownLocation()
  }

  /**
   * Stops all location update requests.
   *
   * This method should be called when location updates are no longer needed
   * to conserve system resources and battery life.
   */
  actual fun stopRequestingLocationUpdates() = locationProvider.stopRequestingLocationUpdates()
}
