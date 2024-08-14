// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import kotlinx.coroutines.flow.Flow

public interface LocationProvider {
  public fun requestLocationUpdates(): Flow<LocationState>
  public suspend fun getLastKnownLocation(): LocationState
  public fun stopRequestingLocationUpdates()
}
