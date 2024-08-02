// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 30f // 30 meters
private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 2).toLong() // 2 minutes

class AndroidLocationProvider(
  private val context: Context,
  private val locationManager: LocationManager = context.getSystemService(
    Context.LOCATION_SERVICE,
  ) as LocationManager,
  private val minutesDistanceChangeUpdates: Float = MIN_DISTANCE_CHANGE_FOR_UPDATES,
  private val minTimeBetweenUpdates: Long = MIN_TIME_BW_UPDATES,
) : LocationProvider {
  private var locationListener: LocationListener? = null

  // Permission already being checked with requestLocation function
  @SuppressLint("MissingPermission")
  override fun observeLocationUpdates(): Flow<LocationState> = callbackFlow {
    locationListener = LocationListener { location ->
      val currentLocation = LocationState.CurrentLocation(
        Point(location.latitude, location.longitude),
      )
      trySend(currentLocation)
    }

    val locationState = requestLocation {
      try {
        when {
          isNetworkEnabled() -> {
            locationManager.requestLocationUpdates(
              LocationManager.NETWORK_PROVIDER,
              minTimeBetweenUpdates,
              minutesDistanceChangeUpdates,
              locationListener!!,
              Looper.getMainLooper(),
            )
          }
          isGPSEnabled() -> {
            locationManager.requestLocationUpdates(
              LocationManager.GPS_PROVIDER,
              minTimeBetweenUpdates,
              minutesDistanceChangeUpdates,
              locationListener!!,
              Looper.getMainLooper(),
            )
          }
          else -> {
            throw IllegalStateException("No location provider available")
          }
        }
      } catch (e: Exception) {
        close(e)
      }
      // This is to satisfy the return type of requestLocation.lambda but the location should be
      // returned in the listener
      LocationState.CurrentLocation(null)
    }

    if (locationState != LocationState.CurrentLocation(null)) trySend(locationState)
    awaitClose { locationManager.removeUpdates(locationListener!!) }
  }

  // Permission already being checked with requestLocation function
  @SuppressLint("MissingPermission")
  override suspend fun getLastKnownLocation(): LocationState {
    return requestLocation {
      val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

      LocationState.CurrentLocation(
        if (location == null) null else Point(location.latitude, location.longitude),
      )
    }
  }

  override fun stopLocating() {
    locationListener?.let { locationManager.removeUpdates(it) }
  }

  private fun isGPSEnabled(): Boolean {
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
  }

  private fun isNetworkEnabled(): Boolean {
    return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
  }

  private suspend fun requestLocation(
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
