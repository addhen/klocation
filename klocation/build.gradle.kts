// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0


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
        implementation(libs.androidx.core.ktx)
        implementation(libs.play.services.location)
        implementation(libs.kotlinx.coroutines.play.services)
      }
    }

    commonTest {
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

publishing {
  // Configure all publications
  publications.withType<MavenPublication> {

    // Provide artifacts information required by Maven Central
    pom {
      name.set("KLocation")
      description.set("A kotlin multiplatform library for getting a device's location")
    }
  }
}
