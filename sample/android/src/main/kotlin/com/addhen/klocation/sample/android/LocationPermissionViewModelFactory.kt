package com.addhen.klocation.sample.android

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.addhen.klocation.sample.shared.permission.LocationPermissionViewModel
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController

@Suppress("UNCHECKED_CAST")
class LocationPermissionViewModelFactory(private val context: Context) :
  ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T =
    LocationPermissionViewModel(
      PermissionsController(context),
      Permission.COARSE_LOCATION,
      Permission.LOCATION,
    ) as T
}
