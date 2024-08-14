// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import kotlinx.coroutines.flow.Flow

public actual class LocationService(
  public actual val locationProvider: LocationProvider = CLLocationProvider(),
) {

  public actual fun requestLocationUpdates(): Flow<LocationState> {
    return locationProvider.requestLocationUpdates()
  }

  public actual suspend fun getLastKnownLocation(): LocationState {
    return locationProvider.getLastKnownLocation()
  }

  public actual fun stopRequestingLocationUpdates(): Unit = locationProvider.stopRequestingLocationUpdates()
}
