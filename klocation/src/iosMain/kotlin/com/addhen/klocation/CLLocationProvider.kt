package com.addhen.klocation

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.WeakReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationAccuracy
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.Foundation.NSLog
import platform.darwin.NSObject

class CLLocationProvider(
  accuracy: CLLocationAccuracy = kCLLocationAccuracyBest
) : LocationProvider {

  private val locationsChannel = Channel<LocationState>(Channel.BUFFERED)
  private val trackerScope = CoroutineScope(Dispatchers.Main)
  private val observeLocationDelegate = ObserveLocationDelegate(locationsChannel, trackerScope)
  private val lastKnownLocationDelegate = LastKnownLocationDelegate()
  private val observeLocationManager = CLLocationManager().apply {
    delegate = observeLocationDelegate
    desiredAccuracy = accuracy
  }

  private val lastKnownLocationManager = observeLocationManager.apply {
    delegate = lastKnownLocationDelegate
  }

  override fun observeLocationUpdates(): Flow<LocationState> {
    observeLocationManager.startUpdatingLocation()
    return callbackFlow {
      locationsChannel.tryReceive()
        .onSuccess { trySend(it) }
        .onFailure { trySend(LocationState.Error(it ?: Throwable())) }
      awaitClose { observeLocationManager.stopUpdatingLocation() }
    }
  }

  override suspend fun getLastKnownLocation(): LocationState = suspendCoroutine { continuation ->
    lastKnownLocationManager.requestLocation()
    lastKnownLocationDelegate.locationCallback = { locationState ->
      continuation.resume(locationState)
    }
  }

  override fun stopLocating() {
    observeLocationManager.stopUpdatingLocation()
    lastKnownLocationManager.stopUpdatingLocation()
  }

  @OptIn(ExperimentalNativeApi::class)
  private class ObserveLocationDelegate(
    locationsChannel: Channel<LocationState>,
    scope: CoroutineScope
  ) : NSObject(), CLLocationManagerDelegateProtocol {
    private val coroutineScope = WeakReference(scope)
    private val locationsChannel = WeakReference(locationsChannel)

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
      val locations = didUpdateLocations as List<CLLocation>
      val scope = coroutineScope.get() ?: return
      val locationsChannel = locationsChannel.get() ?: return
      locations.forEach { location ->
        val locationState = LocationState.CurrentLocation(location)
        scope.launch { locationsChannel.send(locationState) }
      }
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
      NSLog("$this fail with $didFailWithError")
      val scope = coroutineScope.get() ?: return
      val locationsChannel = locationsChannel.get() ?: return
      scope.launch {
        locationsChannel.send(LocationState.Error(Throwable(didFailWithError.description)))
      }
    }
  }

  private class LastKnownLocationDelegate : NSObject(), CLLocationManagerDelegateProtocol {
    var locationCallback: ((LocationState) -> Unit)? = null

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
      val locations = didUpdateLocations as List<CLLocation>
      val locationState = LocationState.CurrentLocation(locations.lastOrNull())
      locationCallback?.invoke(locationState)
      locationCallback = null
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
      NSLog("$this fail with $didFailWithError")
      locationCallback?.invoke(LocationState.Error(Throwable(didFailWithError.description)))
      locationCallback = null
    }
  }
}
