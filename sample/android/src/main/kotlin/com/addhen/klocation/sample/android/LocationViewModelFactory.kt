// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0

package com.addhen.klocation.sample.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.addhen.klocation.LocationService
import com.addhen.klocation.sample.shared.LocationViewModel

@Suppress("UNCHECKED_CAST")
class LocationViewModelFactory(
  private val locationService: LocationService,
) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return LocationViewModel(locationService) as T
  }
}
