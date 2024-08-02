plugins {
  id("convention.plugin.android.library")
  id("convention.plugin.kotlin.multiplatform")
  id("org.jetbrains.dokka")
  id("convention.plugin.maven.publication")
  id("convention.plugin.metalava")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(libs.kotlinx.coroutines.core)
      }
    }

    androidMain {
      dependencies {
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(libs.kotlin.test)
      }
    }
  }
}

android {
  namespace = "com.addhen.klocation"
  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }
}
