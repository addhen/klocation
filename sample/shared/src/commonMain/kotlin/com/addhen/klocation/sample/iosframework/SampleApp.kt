package com.addhen.klocation.sample.iosframework

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.addhen.klocation.sample.iosframework.component.AppSurface
import com.addhen.klocation.sample.iosframework.navigation.AppNavGraph
import com.addhen.klocation.sample.iosframework.navigation.LocationPermissionRoute
import com.addhen.klocation.sample.iosframework.navigation.LocationRoute
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlin.reflect.KClass


@Composable
public fun SampleApp(
  navController: NavHostController,
  permissionsController: PermissionsController,
  permissionScreen: @Composable () -> Unit,
  locationScreen: @Composable () -> Unit
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
        locationScreen = locationScreen
      )
    }
  }
}
