// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
plugins {
  id("convention.plugin.android.library")
  id("convention.plugin.kotlin.multiplatform")
  id("convention.plugin.compose")
  id("org.jetbrains.dokka")
  id("convention.plugin.maven.publication")
  id("convention.plugin.metalava")
}

android {
  namespace = "com.addhen.klocation.compose"
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(projects.klocation)
        implementation(compose.foundation)
      }
    }
  }
}
