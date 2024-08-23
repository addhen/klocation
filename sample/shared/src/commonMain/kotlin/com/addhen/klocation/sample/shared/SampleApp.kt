// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation.sample.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.addhen.klocation.sample.shared.component.AppSurface
import com.addhen.klocation.sample.shared.navigation.AppNavGraph
import com.addhen.klocation.sample.shared.navigation.LocationPermissionRoute
import com.addhen.klocation.sample.shared.navigation.LocationRoute
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlin.reflect.KClass

@Composable
public fun SampleApp(
  navController: NavHostController,
  permissionsController: PermissionsController,
  permissionScreen: @Composable () -> Unit,
  locationScreen: @Composable () -> Unit,
) {
  SamplesTheme {
    var hasAllPermissionGranted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
      val hasCoarseLocation = permissionsController.isPermissionGranted(Permission.COARSE_LOCATION)
      val hasFineLocation = permissionsController.isPermissionGranted(Permission.LOCATION)

      hasAllPermissionGranted = hasCoarseLocation && hasFineLocation
    }
    AppSurface {
      val startDestination: KClass<*> =
        if (hasAllPermissionGranted) {
          LocationRoute::class
        } else {
          LocationPermissionRoute::class
        }

      AppNavGraph(
        navController = navController,
        startDestination,
        permissionScreen = permissionScreen,
        locationScreen = locationScreen,
      )
    }
  }
}
