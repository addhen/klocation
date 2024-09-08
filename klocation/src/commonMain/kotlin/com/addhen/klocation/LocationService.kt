// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0

package com.addhen.klocation

import kotlinx.coroutines.flow.Flow

/**
 * This class provides a platform-agnostic interface for location-related operations.
 *
 * This class is marked as `expect` meaning that it is intended to be implemented by platform-specific
 * implementations to provide the actual functionality.
 */
public expect class LocationService {

  /**
   * The underlying LocationProvider used for location operations.
   *
   * This property allows access to the platform-specific location provider,
   * which handles the low-level details of location services.
   */
  public val locationProvider: LocationProvider

  /**
   * Requests continuous location updates.
   *
   * @return A [Flow] of [LocationState] representing the stream of location updates.
   * The flow will emit new states as the device's location changes or if there are any errors.
   *
   * Possible [LocationState] values include:
   * - [LocationState.CurrentLocation]: Contains the latest location information.
   * - [LocationState.PermissionMissing]: Indicates that location permissions are not granted.
   * - [LocationState.LocationDisabled]: Indicates that device location services are turned off.
   * - [LocationState.NoNetworkEnabled]: Indicates that network services required for location are unavailable.
   * - [LocationState.Error]: Represents any errors that occurred during location requests.
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
