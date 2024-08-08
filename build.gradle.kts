// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0

plugins {
  id("convention.plugin.root")
  id("convention.plugin.nexus.publication")
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library).apply(false)
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.android.lint) apply false
  alias(libs.plugins.android.test) apply false
  alias(libs.plugins.spotless) apply false
  alias(libs.plugins.metalava) apply false
  alias(libs.plugins.compose.multiplatform) apply false
  alias(libs.plugins.compose.compiler) apply false
}
