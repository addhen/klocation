// Copyright 2025, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0

package com.addhen.klocation.sample.android

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.addhen.klocation.sample.shared.permission.LocationPermissionViewModel
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.location.COARSE_LOCATION
import dev.icerock.moko.permissions.location.LOCATION

@Suppress("UNCHECKED_CAST")
class LocationPermissionViewModelFactory(private val context: Context) :
  ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T = LocationPermissionViewModel(
    PermissionsController(context),
    Permission.COARSE_LOCATION,
    Permission.LOCATION,
  ) as T
}
