// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation.sample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.addhen.klocation.sample.android.component.AppSurface
import com.addhen.klocation.sample.android.navigation.AppNavGraph
import com.addhen.klocation.sample.android.navigation.LocationPermissionRoute
import com.addhen.klocation.sample.android.navigation.LocationRoute
import com.addhen.klocation.sample.shared.SamplesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlin.reflect.KClass

class MainActivity : ComponentActivity() {
  @OptIn(ExperimentalPermissionsApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    setContent {
      SamplesTheme {
        val locationPermissionsState = rememberMultiplePermissionsState(
          listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
          ),
        )
        val navController = rememberNavController()
        AppSurface {
          val startDestination: KClass<*> =
            if (locationPermissionsState.allPermissionsGranted) {
              LocationRoute::class
            } else {
              LocationPermissionRoute::class
            }

          AppNavGraph(navController = navController, startDestination)
        }
      }
    }
  }
}
