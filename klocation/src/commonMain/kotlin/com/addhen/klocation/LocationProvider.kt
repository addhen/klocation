package com.addhen.klocation

import kotlinx.coroutines.flow.Flow

interface LocationProvider {
  fun observeLocationUpdates(): Flow<LocationState>
  suspend fun getLastKnownLocation(): LocationState
  fun stopLocating()
}
