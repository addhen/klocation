// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

/**
 * Represents the various states of a location request.
 *
 * This sealed interface encapsulates all possible outcomes when requesting location information,
 * including success states, error conditions, and system status indicators.
 */
public sealed interface LocationState {
  /**
   * Indicates that the application lacks the necessary permissions to access location services.
   *
   * This state is returned when the user has not granted location permissions to the app.
   * To resolve this, prompt the user to grant location permissions in the app settings.
   */
  public data object PermissionMissing : LocationState

  /**
   * Indicates that the device's location services are disabled.
   *
   * This state is returned when the user has turned off location services at the device level.
   * To resolve this, guide the user to enable location services in the device settings.
   */
  public data object LocationDisabled : LocationState

  /**
   * Indicates that no network connection is available for location services.
   *
   * This state is returned when the device cannot connect to network services required for
   * certain location features. To resolve this, ensure the device has an active internet connection.
   */
  public data object NoNetworkEnabled : LocationState

  /**
   * Represents a successfully retrieved location.
   *
   * @param T The type of the location object, which may vary depending on the platform.
   * @param location The platform specific location object, which may be null if no location data is available.
   */
  public data class CurrentLocation<out T>(val location: T?) : LocationState

  /**
   * Represents an error that occurred during the location request process.
   *
   * @param cause The [Throwable] that caused the error, providing details about what went wrong.
   */
  public data class Error(val cause: Throwable) : LocationState
}
