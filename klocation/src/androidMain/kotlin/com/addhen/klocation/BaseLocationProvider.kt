// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat

/**
 * A [LocationProvider] using Android in-built location classes for location-related update using
 * Kotlin Flow.
 *
 * It handles the complexities of interacting with the Android location services and provides a
 * clean APIs to work with. Use this location provider if you don't want to use Google Play services
 * or don't support Google Play services
 *
 * @property context The Android context used for accessing system services.
 * @property locationManager The location manager
 * @property minTimeMs    minimum time interval between location updates in milliseconds
 * @property minDistanceMeters minimum distance between location updates in meters
 * @property locationManager     the listener to receive location updates
 */
abstract class BaseLocationProvider(
  private val context: Context,
  protected val locationManager: LocationManager = context.getSystemService(
    Context.LOCATION_SERVICE,
  ) as LocationManager,
) {

  protected fun isGPSEnabled(): Boolean {
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
  }

  protected fun isNetworkEnabled(): Boolean {
    return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
  }

  protected suspend fun requestLocation(
    requestLocation: suspend() -> LocationState.CurrentLocation,
  ): LocationState {
    return runCatching {
      if (ActivityCompat.checkSelfPermission(
          context,
          Manifest.permission.ACCESS_FINE_LOCATION,
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
          context,
          Manifest.permission.ACCESS_COARSE_LOCATION,
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        LocationState.PermissionMissing
      } else if (isGPSEnabled()) {
        LocationState.LocationDisabled
      } else if (isNetworkEnabled()) {
        LocationState.NoNetworkEnabled
      } else {
        requestLocation()
      }
    }.getOrElse {
      LocationState.Error(it.message ?: "")
    }
  }
}
