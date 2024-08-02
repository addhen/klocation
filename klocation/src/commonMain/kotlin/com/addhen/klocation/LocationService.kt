package com.addhen.klocation

expect class LocationService() {
  fun observeLocationUpdates(): Flow<LocationState>
  suspend fun getLastKnownLocation(): LocationState
  fun stopLocating()
}
