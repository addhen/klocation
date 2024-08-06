// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await

/**
 * A [LocationProvider] using Google Play services FusedLocationProviderClient for more
 * efficient and accurate location updates. If you care about efficiency and accuracy and not an
 * issue to rely on Google play services, the [FusedLocationProvider] is recommended.
 *
 * @property context The Android context used for accessing system services.
 * @property locationManager The Android system's location manager.
 * @property interval The rate in milliseconds at which the app prefers to receive location updates.
 *                    The default is 1000 milliseconds.
 * @property priority The priority of the request, which gives the Google Play services location
 *                    services a strong hint about which location sources to use. The default value
 *                    is [Priority.PRIORITY_HIGH_ACCURACY].
 */
class FusedLocationProvider(
  context: Context,
  intervalMs: Long = 1000,
  private val priority: Int = Priority.PRIORITY_HIGH_ACCURACY,
) : BaseLocationProvider<Location>(context) {

  private val locationProviderClient: FusedLocationProviderClient =
    LocationServices.getFusedLocationProviderClient(context)
  private val locationRequest = LocationRequest.Builder(priority, intervalMs).build()
  private var locationCallback: LocationCallback? = null

  /**
   * Provides a flow [LocationState] of location updates.
   *
   * This method sets up a continuous stream of location updates using either the network
   * or GPS provider, depending on availability. It automatically manages the lifecycle
   * of the location updates, stopping them when the flow is cancelled.
   *
   * @return A [Flow] emitting [LocationState] states as they become available.
   * @throws IllegalStateException if no location provider is available.
   */
  // Permission already being checked with requestLocation function
  @SuppressLint("MissingPermission")
  override fun observeLocationUpdates(): Flow<LocationState> = callbackFlow {
    locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult) {
        locationResult.lastLocation?.let { location ->
          trySend(LocationState.CurrentLocation(location))
        }
      }
    }

    val locationState = requestLocation {
      locationCallback?.let { callback ->
        locationProviderClient.requestLocationUpdates(locationRequest, callback, null)
        // This is to satisfy the return type of requestLocation.lambda but the location should be
        // emitted in the listener already.
      }
      LocationState.CurrentLocation(null)
    }

    if (locationState != LocationState.CurrentLocation(null)) trySend(locationState)
    awaitClose { stopLocating() }
  }.catch { cause: Throwable ->
    // Handle any exceptions that occur during flow collection.
    emit(LocationState.Error(cause))
  }

  /**
   * Retrieves the last known location from the device.
   *
   * This method attempts to get the last known location from the network provider first,
   * and if that's not available, it tries the GPS provider.
   *
   * @return The last known [LocationState], or a [LocationState.CurrentLocation]
   * with a `null` [Point] if no location is available.
   */
  // Permission already being checked with requestLocation function
  @SuppressLint("MissingPermission")
  override suspend fun getLastKnownLocation(): LocationState {
    return requestLocation {
      val request = CurrentLocationRequest.Builder()
        .setPriority(priority)
        .build()
      val location = locationProviderClient.getCurrentLocation(request, null).await()
      LocationState.CurrentLocation(location)
    }
  }

  override fun stopLocating() {
    locationCallback?.let { locationProviderClient.removeLocationUpdates(it) }
    locationCallback = null
  }
}
