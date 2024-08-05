plugins {
  id("convention.plugin.android.library")
  id("convention.plugin.kotlin.multiplatform")
  id("convention.plugin.compose")
}

android {
  namespace = "com.addhen.klocation.sample.shared"
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(compose.material3)
      }
    }
  }
}
