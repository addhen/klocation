// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import kotlinx.coroutines.flow.Flow

expect class LocationService {
  val locationProvider: LocationProvider
  fun requestLocationUpdates(): Flow<LocationState>
  suspend fun getLastKnownLocation(): LocationState
  fun stopRequestingLocationUpdates()
}
