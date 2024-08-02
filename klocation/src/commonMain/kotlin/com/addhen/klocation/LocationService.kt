package com.addhen.klocation

import kotlinx.coroutines.flow.Flow

expect class LocationService {
  val locationProvider: LocationProvider
  fun observeLocationUpdates(): Flow<LocationState>
  suspend fun getLastKnownLocation(): LocationState
  fun stopLocating()
}
