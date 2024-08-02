// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import kotlinx.coroutines.flow.Flow

interface LocationProvider {
  fun observeLocationUpdates(): Flow<LocationState>
  suspend fun getLastKnownLocation(): LocationState
  fun stopLocating()
}
