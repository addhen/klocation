// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation.sample.android

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.addhen.klocation.FusedLocationProvider
import com.addhen.klocation.LocationService
import com.addhen.klocation.sample.iosframework.LocationScreen
import com.addhen.klocation.sample.iosframework.LocationViewModel
import com.addhen.klocation.sample.iosframework.SampleApp
import com.addhen.klocation.sample.iosframework.navigation.LocationRoute
import com.addhen.klocation.sample.iosframework.navigation.buildNavOptions
import com.addhen.klocation.sample.iosframework.permission.LocationPermissionScreen
import com.addhen.klocation.sample.iosframework.permission.LocationPermissionViewModel
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    val permissionsController = PermissionsController(this)
    permissionsController.bind(this)
    setContent {
      val navController = rememberNavController()
      SampleApp(
        navController,
        permissionsController,
        permissionScreen = {
          val viewModel = LocationPermissionViewModel(
            PermissionsController(LocalContext.current),
            Permission.COARSE_LOCATION,
            Permission.LOCATION,
          )
          LocationPermissionScreen(viewModel) {
            navController.navigate(LocationRoute, navController.buildNavOptions())
          }
        },
        locationScreen = {
          val locationService = LocationService(FusedLocationProvider(LocalContext.current))
          val viewModel = LocationViewModelFactory(
            locationService = locationService,
          ).create(LocationViewModel::class.java)
          LocationScreen(viewModel, locationService) { locationState ->
            val location = locationState.location as? Location
            "${location?.latitude},${location?.longitude}"
          }
        }
      )
    }
  }
}
