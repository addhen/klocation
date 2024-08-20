// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation.sample.shared.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.addhen.klocation.sample.android.navigation.LocationPermissionRoute
import com.addhen.klocation.sample.android.navigation.LocationRoute
import kotlin.reflect.KClass

@Composable
public fun AppNavGraph(
  navController: NavHostController,
  startDestination: KClass<*>,
  locationScreen: @Composable () -> Unit,
) {
  NavHost(
    navController = navController,
    startDestination = startDestination,
  ) {
    composable<LocationPermissionRoute> {
      ///LocationPermissionScreen()
    }

    composable<LocationRoute> {
      locationScreen()
    }
  }
}
