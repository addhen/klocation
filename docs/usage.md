Usage
=====

There two main ways to use KLocation. Either by using the `klocation` artifact or the `klocation-compose` artifact.

## klcoation artifact

## klocation-compose artifact

This guide will walk you through using the APIs provided for consuming location in your
Compose-based application.

### Requesting location updates
To receive continuous location updates, use the `locationUpdatesState()` function in your Composable:

```kotlin

import com.addhen.klocation.compose.locationUpdatesState

Composable
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
      // for Android cast as `android.location.Location` for iOS cast as `CLLocation`
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
fun RefreshableLastLocation(locationService: LocationService, refreshTrigger: Boolean) {
    val lastLocationState by lastKnowLocationState(locationService, refreshTrigger)
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
