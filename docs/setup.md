Setup KLocation
===============

KLocation comes with two artifacts: `klocation` and `klocation-compose`. The `klocation` artifact
is a Kotlin Multiplatform library that provides location services for Android and iOS.
The `klocation-compose` artifact is a Compose Multiplatform library that provides simple APIs for
consuming the location services as a Compose [State](https://developer.android.com/develop/ui/compose/state).

## Installation

To use `klocation` in your project, add the following dependencies to your build.gradle file:

```kotlin
dependencies {
  implementation("com.addhen:klocation:<version>")
}
```

For `klocation-compose`, add the following dependencies to your build.gradle file:

```kotlin
dependencies {
  implementation("com.addhen:klocation-compose:<version>")
}
```
## Note

If you are using the `klocation-compose` artifact, you don't need to add the `klocation` artifact.
