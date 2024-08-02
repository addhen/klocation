package com.addhen.klocation

public interface LocationProvider {
  fun observeLocationUpdates(): Flow<LocationState>
  suspend fun getLastKnownLocation(): LocationState
  fun stopLocating()
}
