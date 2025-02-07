// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0

package com.addhen.klocation.sample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.addhen.klocation.FusedLocationProvider
import com.addhen.klocation.LocationService
import com.addhen.klocation.location
import com.addhen.klocation.sample.shared.LocationScreen
import com.addhen.klocation.sample.shared.LocationViewModel
import com.addhen.klocation.sample.shared.SampleApp
import com.addhen.klocation.sample.shared.permission.LocationPermissionScreen
import com.addhen.klocation.sample.shared.permission.LocationPermissionViewModel
import dev.icerock.moko.permissions.PermissionsController

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    val permissionsController = PermissionsController(this)
    permissionsController.bind(this)
    setContent {
      val navController = rememberNavController()
      val locationPermissionViewModel = viewModel<LocationPermissionViewModel>(
        factory = LocationPermissionViewModelFactory(LocalContext.current)
      )
      val locationService = LocationService(FusedLocationProvider(LocalContext.current))
      val locationViewModel = viewModel<LocationViewModel>(factory =
        LocationViewModelFactory(locationService = locationService)
      )
      SampleApp(
        navController,
        permissionsController,
        permissionScreen = {
          LocationPermissionScreen(locationPermissionViewModel, navController)
        },
        locationScreen = {
          LocationScreen(
            locationViewModel,
            LocationService(FusedLocationProvider(LocalContext.current)),
          ) { locationState ->
            val location = locationState.location
            "${location?.latitude},${location?.longitude}"
          }
        },
      )
    }
  }
}
