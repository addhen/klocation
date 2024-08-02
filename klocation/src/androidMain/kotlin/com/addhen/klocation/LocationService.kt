// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import android.content.Context
import kotlinx.coroutines.flow.Flow

actual class LocationService(
  private val context: Context,
  actual val locationProvider: LocationProvider = AndroidLocationProvider(context),
) {

  actual fun observeLocationUpdates(): Flow<LocationState> {
    return locationProvider.observeLocationUpdates()
  }

  actual suspend fun getLastKnownLocation(): LocationState {
    return locationProvider.getLastKnownLocation()
  }

  actual fun stopLocating() = locationProvider.stopLocating()
}
