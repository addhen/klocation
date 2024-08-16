Usage
=====

They come with a `LocationService` class which provides a high-level implementations for handling
location-related operations in your applications. It uses a `LocationProvider` to manage the actual
location updates, defaulting to `AndroidLocationProvider` if not specified.

## Initialization
To use `LocationService`, you first need to initialize it. There are few ways to do this:

### Using the default provider

```kotlin
import com.addhen.klocation.LocationService

val context: Context = /* your application context */
val locationService = LocationService(context)
```

This method uses the default `AndroidLocationProvider`.

### Using the FusedLocationProvider

The `FusedLocationProvider` is a wrapper around the Google Play services
`FusedLocationProviderClient` API. To use it:

Add the following dependencies to your Gradle build file:

```kotlin
dependencies {
  implementation 'com.google.android.gms:play-services-location:<version>'
  // Add other required dependencies
}
```
Initialize the `FusedLocationProvider`:

```kotlin
val context: Context = // your application context
val fusedLocationProvider = FusedLocationProvider(
    context = context,
    intervalMs = 1000, // Update interval in milliseconds (optional)
    priority = Priority.PRIORITY_HIGH_ACCURACY // Location priority (optional)
)
```

You can then pass the `fusedLocationProvider` to the `LocationService`:

```kotlin
val locationService = LocationService(fusedLocationProvider)
```

### Using a custom location provider

```kotlin
val customProvider: LocationProvider = // your custom location provider
val locationService = LocationService(locationProvider = customProvider)
```

### Using the `LocationService`

The library comes with two artifacts: `klocation` and `klocation-compose`. They both come with
the `LocationService` class.

## For klcoation artifact
This guide will walk you through using the `LocationService` APIs for consuming location in your
non compose based applications.

### Requesting location updates
To receive continuous location updates, use the `requestLocationUpdates()` method:

```kotlin
locationService.requestLocationUpdates()
  .distinctUntilChanged()
  .onEach { state ->
    // Handle the location state
    when (state) {
      is LocationState.LocationDisabled -> {
        // Location is disabled
      }
      is LocationState.Error -> {
        // Handle the error
      }
      LocationState.NoNetworkEnabled -> {
        // No network enabled
      }
      LocationState.PermissionMissing -> {
        // Permission missing
      }
      is LocationState.CurrentLocation<*> -> {
        // Handle the current location
        // for Android cast as `android.location.Location`
        // for iOS cast as `CLLocation`
        val location = state.location as? Location
        // Do something with the location
      }
    }
  }.launchIn(viewModelScope)
```

### Getting the last known location
To retrieve the last known location, use the `getLastKnownLocation()` method:

```kotlin
viewModelScope.launch {
  try {
    val locationState = locationService.getLastKnownLocation()
    // Handle the location state
    when (locationState) {
      is LocationState.LocationDisabled -> {
        // Location is disabled
      }
      is LocationState.Error -> {
        // Handle the error
      }
      LocationState.NoNetworkEnabled -> {
        // No network enabled
      }
      LocationState.PermissionMissing -> {
        // Permission missing
      }
      is LocationState.CurrentLocation<*> -> {
        // Handle the current location
        // for Android cast as `android.location.Location`
        // for iOS cast as `CLLocation`
        val location = locationState.location as? Location
        // Do something with the location
      }
    }
  } catch (e: Throwable) {
    ensureActive()
    // Handle the error
  }
}
```

## For klocation-compose artifact

This guide will walk you through using the APIs provided for consuming location in your
Compose-based application.

### Requesting location updates
To receive continuous location updates, use the `locationUpdatesState()` function in your Composable:

```kotlin

import com.addhen.klocation.compose.locationUpdatesState

@Composable
fun LocationTracking(locationService: LocationService) {
  val locationState by locationUpdatesState(locationService)

  val info = getLocation(locationState)
  Text(text = info)
}

@Composable
private fun getLocation(locationState: LocationState): String {
  val locationInfo = when (locationState) {
    is LocationState.LocationDisabled -> {
      "Location disabled"
    }
    is LocationState.Error -> {
      "Error: ${(locationState as LocationState.Error).message}"
    }
    LocationState.NoNetworkEnabled -> {
      "No network enabled"
    }
    LocationState.PermissionMissing -> {
      "Permission missing"
    }

    is LocationState.CurrentLocation<*> -> {
      // for Android cast as `android.location.Location`
      // for iOS cast as `CLLocation`
      val location = locationState.location as? Location
      "${location?.latitude},${location?.longitude}"
    }
  }
  return locationInfo
}
```

### Getting the last known location
To get the last known location, use the `lastKnownLocationState()` function in your Composable:

```kotlin
import com.addhen.klocation.compose.lastKnownLocationState

@Composable
fun LastKnownLocation(locationService: LocationService) {
  val locationState by lastKnownLocationState(locationService)

  val info = getLocation(locationState)
  Text(text = info)
}
```

### Using recomposition keys
The `lastKnownLocationState()` function has `overloads` that accept recomposition keys. These are
useful when you want to force a refresh of the last known location based on certain conditions:

```kotlin

import com.addhen.klocation.compose.lastKnownLocationState

@Composable
fun RefreshableLastLocation(
  locationService: LocationService,
  refreshTrigger: Boolean
) {
    val lastLocationState by lastKnowLocationState(
      locationService,
      refreshTrigger
    )
    // Use lastLocationState as before...
}
```
In this example, the last known location will be refreshed whenever `refreshTrigger` changes.

### Best practices
1. **Permission Handling:** Ensure you have the necessary location permissions before using these functions.
2. **Error Handling:** Always handle the LocationState.Error case to provide a good user experience.
3. **Lifecycle Awareness:** These Composable functions are automatically lifecycle-aware, but ensure you're not calling them unnecessarily.
4. **Performance:** Use lastKnownLocationState() when you don't need continuous updates, as it's less resource-intensive.
5. **Recomposition Keys:** Use keys judiciously. Overusing them can lead to unnecessary recompositions and battery drain.
