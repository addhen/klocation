// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation.sample.android

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.addhen.klocation.LocationService
import com.addhen.klocation.LocationState
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationViewModel(
  private val locationService: LocationService,
) : ViewModel() {
  private val viewStateEmitter =
    MutableStateFlow(LocationUiState(flag = LocationUiState.Flag.LOADING))

  val viewState: StateFlow<LocationUiState> = viewStateEmitter
    .stateIn(
      viewModelScope,
      started = SharingStarted.WhileSubscribed(5_000),
      viewStateEmitter.value,
    )

  init {
    locationService.observeLocationUpdates()
      .distinctUntilChanged()
      .onEach { state ->
        Log.d(LocationViewModel::class.simpleName, "state $state")
        viewStateEmitter.update { currentUiState ->
          currentUiState.copy(
            flag = LocationUiState.Flag.IDLE,
            observeLocationState = state,
          )
        }
      }.launchIn(viewModelScope)
  }

  fun getLastKnowLocation() {
    viewModelScope.launch {
      try {
        val locationState = locationService.getLastKnownLocation()
        Log.d(LocationViewModel::class.simpleName, "lastKnownLocation $locationState")
        viewStateEmitter.update { currentUiState ->
          currentUiState.copy(
            flag = LocationUiState.Flag.IDLE,
            lastKnowLocationState = locationState,
          )
        }
      } catch (e: Throwable) {
        if (e is CancellationException) throw e
        viewStateEmitter.update { it.copy(flag = LocationUiState.Flag.ERROR) }
      }
    }
  }

  fun stopLocating() = locationService.stopLocating()

  data class LocationUiState(
    val observeLocationState: LocationState = LocationState.CurrentLocation(null),
    val lastKnowLocationState: LocationState = LocationState.CurrentLocation(null),
    val flag: Flag = Flag.IDLE,
  ) {
    enum class Flag {
      LOADING,
      ERROR,
      IDLE,
    }
  }
}

@Suppress("UNCHECKED_CAST")
class LocationViewModelFactory(
  private val locationService: LocationService,
) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return LocationViewModel(locationService) as T
  }
}
