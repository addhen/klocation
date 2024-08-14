// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import kotlinx.coroutines.flow.Flow

actual class LocationService(
  actual val locationProvider: LocationProvider = CLLocationProvider(),
) {

  actual fun requestLocationUpdates(): Flow<LocationState> {
    return locationProvider.requestLocationUpdates()
  }

  actual suspend fun getLastKnownLocation(): LocationState {
    return locationProvider.getLastKnownLocation()
  }

  actual fun stopRequestingLocationUpdates() = locationProvider.stopRequestingLocationUpdates()
}
