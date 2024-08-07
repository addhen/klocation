// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch

private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 30f // 30 meters
private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 2).toLong() // 2 minutes

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
class AndroidLocationProvider(
  private val context: Context,
  private val minDistanceMeters: Float = MIN_DISTANCE_CHANGE_FOR_UPDATES,
  private val minTimeMs: Long = MIN_TIME_BW_UPDATES,
) : BaseLocationProvider<Location>(context) {

  private var locationListener: LocationListener? = null

  /**
   * Provides a flow [LocationState] of location updates.
   *
   * This method sets up a continuous stream of location updates using either the network
   * or GPS provider, depending on availability. It automatically manages the lifecycle
   * of the location updates, stopping them when the flow is cancelled.
   *
   * ```
   * viewModelScope.launch {
   *   locationService.observeLocationUpdates()
   *     .distinctUntilChanged()
   *     .collectLatest { locationState: LocationState ->
   *       when( locationState) {
   *         is LocationState.CurrentLocation<*> -> {
   *           val location = locationState.location as? Location
   *             println("${location?.latitude},${location?.longitude}")
   *         }
   *         ...
   *       }
   *     }
   * }
   * ```
   *
   * @return A [Flow] emitting [LocationState] states as they become available.
   * @throws IllegalStateException if no location provider is available.
   */
  // Permission already being checked with requestLocation function
  @SuppressLint("MissingPermission")
  override fun observeLocationUpdates(): Flow<LocationState> = callbackFlow {
    locationListener = LocationListener { location ->
      val currentLocation = LocationState.CurrentLocation(location)
      trySend(currentLocation)
    }

    val locationState = requestLocation {
      try {
        val provider = when {
          isGPSEnabled() -> LocationManager.GPS_PROVIDER
          isNetworkEnabled() -> LocationManager.NETWORK_PROVIDER
          else -> throw IllegalStateException("No location provider available")
        }

        locationListener?.let { listener ->
          locationManager.requestLocationUpdates(
            provider,
            minTimeMs,
            minDistanceMeters,
            listener,
            Looper.getMainLooper(),
          )
        }
      } catch (cause: Throwable) {
        trySend(LocationState.Error(cause))
      }
      // This is to satisfy the return type of requestLocation.lambda but the location should be
      // emitted in the listener
      LocationState.CurrentLocation(null)
    }

    if (locationState != LocationState.CurrentLocation(null)) trySend(locationState)
    awaitClose { stopLocating() }
  }.catch { cause: Throwable ->
    emit(LocationState.Error(cause))
  }

  /**
   * Retrieves the last known location from the device.
   *
   * This method attempts to get the last known location from the network provider first,
   * and if that's not available, it tries the GPS provider.
   *
   * When a location is obtained it returns the [LocationState.CurrentLocation] with a [Location]
   * which means at use site due to type erasure, you will need to cast the
   * `LocationState#CurrentLocation#location` to a [Location] object. Eg at use-site will be:
   *
   * ```
   * viewModelScope.launch {
   *   try {
   *     val locationState = locationService.getLastKnownLocation()
   *     when( locationState) {
   *       is LocationState.CurrentLocation<*> -> {
   *         val location = locationState.location as? Location
   *         println("${location?.latitude},${location?.longitude}")
   *       }
   *       ...
   *     }
   *   } catch(cause: Throwable) {
   *      // handle exception
   *   }
   * }
   * ```
   *
   * @return The last known [LocationState.CurrentLocation], or a [LocationState.CurrentLocation]
   * with a `null` [Location] if no location is available.
   */
  // Permission already being checked with requestLocation function
  @SuppressLint("MissingPermission")
  override suspend fun getLastKnownLocation(): LocationState {
    return requestLocation {
      val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
      val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

      val bestLocation = when {
        networkLocation != null && gpsLocation != null ->
          if (gpsLocation.time > networkLocation.time) gpsLocation else networkLocation

        gpsLocation != null -> gpsLocation
        networkLocation != null -> networkLocation
        else -> null
      }

      LocationState.CurrentLocation(bestLocation)
    }
  }

  /**
   * Stops all location update requests.
   */
  override fun stopLocating() {
    locationListener?.let { locationManager.removeUpdates(it) }
    locationListener = null
  }
}
