Setup KLocation
===============

KLocation comes with two artifacts: `klocation` and `klocation-compose`. The `klocation` artifact
is a Kotlin Multiplatform library that provides location services for Android and iOS.
The `klocation-compose` artifact is a Compose Multiplatform library that provides simple APIs for
consuming the location services as a compose [State](https://developer.android.com/develop/ui/compose/state).

## Installation

To use `klocation` artifact in your project, add the following dependencies to your gradle build file:

```kotlin
dependencies {
  implementation("com.addhen.klocation:klocation:1.0.0")
}
```

For `klocation-compose` artifact, add the following dependencies to your gradle build file:

```kotlin
dependencies {
  implementation("androidx.compose.runtime:runtime:1.0.0")
  implementation("com.addhen.klocation:klocation-compose:1.0.0")
}
```
## Note

If you are using the `klocation-compose` artifact, you don't need to add the `klocation` artifact.

<details>
<summary>Snapshots of the development version are available in Sonatype's snapshots repository.</summary>
<p>

```groovy
repository {
  mavenCentral()
  maven {
    url 'https://oss.sonatype.org/content/repositories/snapshots/'
  }
}
dependencies {
  implementation("com.addhen.klocation:klocation:1.1.0-SNAPSHOT")
  implementation("com.addhen.klocation:klocation-compose:1.1.0-SNAPSHOT")
}
```

</p>
</details>
