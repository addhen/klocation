// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.WeakReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationAccuracy
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.Foundation.NSLog
import platform.darwin.NSObject

/**
 * This class implements the [LocationProvider] interface for iOS.
 * It manages location updates and permissions using the Core Location framework.
 *
 * @param accuracy The desired location accuracy. Defaults to [kCLLocationAccuracyBest].
 */
public class CLLocationProvider(
  accuracy: CLLocationAccuracy = kCLLocationAccuracyBest,
) : LocationProvider {

  private val locationsChannel = Channel<LocationState>(Channel.BUFFERED)
  private val trackerScope = CoroutineScope(Dispatchers.Main)
  private val observeLocationDelegate = ObserveLocationDelegate(locationsChannel, trackerScope)
  private val lastKnownLocationDelegate = LastKnownLocationDelegate()
  private val observeLocationManager = CLLocationManager().apply {
    delegate = observeLocationDelegate
    desiredAccuracy = accuracy
  }
  private val lastKnownLocationManager = CLLocationManager().apply {
    delegate = lastKnownLocationDelegate
  }

  /**
   * Requests location updates and returns a [Flow] of [LocationState].
   * Handles permission checks and location manager setup.
   *
   * @return A [Flow] of [LocationState] representing the stream of location updates.
   */
  public override fun requestLocationUpdates(): Flow<LocationState> = channelFlow {
    val localChannel = channel
    when (CLLocationManager.authorizationStatus()) {
      kCLAuthorizationStatusNotDetermined -> {
        observeLocationManager.requestWhenInUseAuthorization()
      }

      kCLAuthorizationStatusRestricted, kCLAuthorizationStatusDenied -> {
        localChannel.send(LocationState.PermissionMissing)
      }

      kCLAuthorizationStatusAuthorizedAlways, kCLAuthorizationStatusAuthorizedWhenInUse -> {
        observeLocationManager.startUpdatingLocation()
      }

      else -> {
        localChannel.send(LocationState.CurrentLocation(null))
      }
    }

    val job = launch {
      while (isActive) {
        localChannel.send(locationsChannel.receive())
      }
    }

    awaitClose {
      job.cancel()
    }
  }

  /**
   * Retrieves the last known location.
   * Handles permission checks and returns appropriate [LocationState].
   *
   * @return A [LocationState] representing the last known location or an error state.
   */
  public override suspend fun getLastKnownLocation(): LocationState =
    suspendCoroutine { continuation ->
      lastKnownLocationDelegate.locationCallback = { locationState ->
        continuation.resume(locationState)
      }

      when (CLLocationManager.authorizationStatus()) {
        kCLAuthorizationStatusNotDetermined -> {
          observeLocationManager.requestWhenInUseAuthorization()
        }
        kCLAuthorizationStatusRestricted, kCLAuthorizationStatusDenied -> {
          continuation.resume(LocationState.PermissionMissing)
        }
        kCLAuthorizationStatusAuthorizedAlways, kCLAuthorizationStatusAuthorizedWhenInUse -> {
          lastKnownLocationManager.requestLocation()
        }
        else -> {
          continuation.resume(LocationState.CurrentLocation(null))
        }
      }
    }

  /**
   * Stops all location update requests.
   */
  public override fun stopRequestingLocationUpdates() {
    observeLocationManager.stopUpdatingLocation()
    lastKnownLocationManager.stopUpdatingLocation()
  }

  @OptIn(ExperimentalNativeApi::class)
  @Suppress("UNCHECKED_CAST")
  private class ObserveLocationDelegate(
    locationsChannel: Channel<LocationState>,
    scope: CoroutineScope,
  ) : NSObject(), CLLocationManagerDelegateProtocol {
    private val coroutineScope = WeakReference(scope)
    private val locationsChannel = WeakReference(locationsChannel)

    public override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
      val locations = didUpdateLocations as List<CLLocation>
      val scope = coroutineScope.get() ?: return
      val locationsChannel = locationsChannel.get() ?: return
      locations.forEach { location ->
        val locationState = LocationState.CurrentLocation(location)
        scope.launch { locationsChannel.send(locationState) }
      }
    }

    public override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
      NSLog("$this fail with $didFailWithError")
      val scope = coroutineScope.get() ?: return
      val locationsChannel = locationsChannel.get() ?: return
      scope.launch {
        locationsChannel.send(LocationState.Error(Throwable(didFailWithError.description)))
      }
    }
  }

  @Suppress("UNCHECKED_CAST")
  private class LastKnownLocationDelegate : NSObject(), CLLocationManagerDelegateProtocol {
    var locationCallback: ((LocationState) -> Unit)? = null

    public override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
      val locations = didUpdateLocations as List<CLLocation>
      val locationState = LocationState.CurrentLocation(locations.lastOrNull())
      locationCallback?.invoke(locationState)
      locationCallback = null
    }

    public override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
      NSLog("$this fail with $didFailWithError")
      locationCallback?.invoke(LocationState.Error(Throwable(didFailWithError.description)))
      locationCallback = null
    }
  }
}
