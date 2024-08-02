package com.addhen.klocation

import kotlinx.coroutines.flow.Flow

actual class LocationService(actual val locationProvider: LocationProvider) {

  actual fun observeLocationUpdates(): Flow<LocationState> {
    return locationProvider.observeLocationUpdates()
  }

  actual suspend fun getLastKnownLocation(): LocationState {
    return locationProvider.getLastKnownLocation()
  }

  actual fun stopLocating() = locationProvider.stopLocating()
}
