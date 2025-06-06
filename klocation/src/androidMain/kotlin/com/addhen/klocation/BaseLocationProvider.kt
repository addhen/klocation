// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0

package com.addhen.klocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat

/**
 * Base class for location-related operations.
 *
 * This abstract class provides a foundation for implementing location-based features
 * using Kotlin coroutines and Flow. Extend this if you want to implement a custom [LocationProvider]
 *
 * @param context The Android context used for accessing system services.
 * @param locationManager The Android [LocationManager] class.
 */
public abstract class BaseLocationProvider<T : Location>(
  private val context: Context,
  protected val locationManager: LocationManager = context
    .getSystemService(Context.LOCATION_SERVICE) as LocationManager,
) : LocationProvider {

  protected fun isGPSEnabled(): Boolean =
    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

  protected fun isNetworkEnabled(): Boolean =
    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

  protected suspend fun requestLocation(
    requestLocation: suspend () -> LocationState,
  ): LocationState = runCatching {
    if (ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION,
      ) != PackageManager.PERMISSION_GRANTED &&
      ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION,
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      LocationState.PermissionMissing
    } else if (isGPSEnabled().not()) {
      LocationState.LocationDisabled
    } else if (isNetworkEnabled().not()) {
      LocationState.NoNetworkEnabled
    } else {
      requestLocation()
    }
  }.getOrElse {
    LocationState.Error(it)
  }
}
