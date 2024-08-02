// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
plugins {
  id("convention.plugin.root")
  id("convention.plugin.root.publication")
  // trick: for the same plugin versions in all sub-modules
  alias(libs.plugins.android.library).apply(false)
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.spotless) apply false
}
