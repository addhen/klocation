// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FusedLocationProvider(
  context: Context,
  interval: Long = 1000,
  private val priority: Int = Priority.PRIORITY_HIGH_ACCURACY,
) : BaseLocationProvider(context), LocationProvider {

  private val locationProviderClient: FusedLocationProviderClient =
    LocationServices.getFusedLocationProviderClient(context)
  private val locationRequest = LocationRequest.Builder(priority, interval).build()
  private lateinit var locationCallback: LocationCallback

  // Permission already being checked with requestLocation function
  @SuppressLint("MissingPermission")
  override fun observeLocationUpdates(): Flow<LocationState> = callbackFlow {
    locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult) {
        locationResult.lastLocation?.let { location ->
          trySend(
            LocationState.CurrentLocation(
              Point(location.latitude, location.longitude),
            ),
          )
        }
      }
    }

    val locationState = requestLocation {
      locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
      // This is to satisfy the return type of requestLocation.lambda but the location should be
      // emitted in the listener
      LocationState.CurrentLocation(null)
    }

    if (locationState != LocationState.CurrentLocation(null)) trySend(locationState)
    awaitClose { locationProviderClient.removeLocationUpdates(locationCallback) }
  }

  // Permission already being checked with requestLocation function
  @SuppressLint("MissingPermission")
  override suspend fun getLastKnownLocation(): LocationState {
    return requestLocation {
      val request = CurrentLocationRequest.Builder()
        .setPriority(priority)
        .build()
      val location = locationProviderClient.getCurrentLocation(request, null).await()
      LocationState.CurrentLocation(
        Point(location.latitude, location.longitude),
      )
    }
  }

  override fun stopLocating() {
    if (::locationCallback.isInitialized) {
      locationProviderClient.removeLocationUpdates(locationCallback)
    }
  }
}
