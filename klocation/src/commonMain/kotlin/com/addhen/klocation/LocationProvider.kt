// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0

package com.addhen.klocation

import kotlinx.coroutines.flow.Flow

/**
 * [LocationProvider] defines the contract for platform-specific location provider implementations.
 *
 * Implementations of this interface are typically not used directly by application code.
 * Instead, they are utilized by the [LocationService] to provide location functionality.
 */
public interface LocationProvider {

  /**
   * Requests continuous location updates.
   *
   * @return A [Flow] of [LocationState] representing the stream of location updates.
   * The flow will emit new states as the device's location changes or if there are any errors.
   */
  public fun requestLocationUpdates(): Flow<LocationState>

  /**
   * Retrieves the last known location.
   *
   * @return A [LocationState] representing the last known location or an error state.
   */
  public suspend fun getLastKnownLocation(): LocationState

  /**
   * Stops requesting location updates.
   *
   * Call this method to stop receiving location updates when they are no longer needed.
   */
  public fun stopRequestingLocationUpdates()
}
