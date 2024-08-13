KLocation comes with two main functions for retrieving location data:

- `getLastKnownLocation()`: This function retrieves the last known location of the device.

**Android**

```kotlin
val locationService = LocationService(FusedLocationProvider(context))
val location = kLocation.getLastKnownLocation()
```

- `requestLocationUpdates()`: This function requests location updates from the device.
```kotlin
LocationService(FusedLocationProvider(context)).observeLocationUpdates()
  .distinctUntilChanged()
  .onEach { locationState ->
    Log.d(LocationViewModel::class.simpleName, "state $state")
  }.launchIn(viewModelScope)
```
