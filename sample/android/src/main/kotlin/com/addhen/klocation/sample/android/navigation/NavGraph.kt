package com.addhen.klocation.sample.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.addhen.klocation.FusedLocationProvider
import com.addhen.klocation.LocationService
import com.addhen.klocation.sample.android.permission.LocationPermissionScreen
import com.addhen.klocation.sample.android.LocationScreen
import com.addhen.klocation.sample.android.LocationViewModel
import com.addhen.klocation.sample.android.LocationViewModelFactory
import kotlin.reflect.KClass

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: KClass<*>) {

  NavHost(
    navController = navController,
    startDestination = startDestination
  ) {

    composable<LocationPermissionRoute> {
      LocationPermissionScreen()
    }

    composable<LocationRoute> {
      val viewModel = LocationViewModelFactory(
        LocationService(LocalContext.current)
      ).create(LocationViewModel::class.java)
      LocationScreen(viewModel)
    }
  }
}
