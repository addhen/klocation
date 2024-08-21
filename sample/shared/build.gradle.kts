import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
plugins {
  id("convention.plugin.android.library")
  id("convention.plugin.kotlin.multiplatform")
  id("convention.plugin.compose")
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "com.addhen.klocation.sample.shared"
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(compose.material3)
        api(projects.klocation)
        api(projects.klocationCompose)
        api(libs.touchlab.kermit)
        implementation(compose.runtime)
        implementation(compose.components.resources)
        implementation(libs.lifecycle.viewmodel.compose)
        implementation(libs.androidx.navigation.compose)
        implementation(libs.kotlinx.serialization)
        api(libs.moko.permissions)
        implementation(libs.moko.permissions.compose)
        implementation(libs.kotlinx.serialization)
      }
    }

    targets.withType<KotlinNativeTarget>().configureEach {
      binaries.framework {
        isStatic = true
        baseName = "KLocationKt"

        export(projects.klocation)
        export(projects.klocationCompose)
      }
    }
  }
}
