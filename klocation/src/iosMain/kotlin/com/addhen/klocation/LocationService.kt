// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import kotlinx.coroutines.flow.Flow

actual class LocationService(
  actual val locationProvider: LocationProvider = CLLocationProvider(),
) {

  actual fun observeLocationUpdates(): Flow<LocationState> {
    return locationProvider.observeLocationUpdates()
  }

  actual suspend fun getLastKnownLocation(): LocationState {
    return locationProvider.getLastKnownLocation()
  }

  actual fun stopLocating() = locationProvider.stopLocating()
}
