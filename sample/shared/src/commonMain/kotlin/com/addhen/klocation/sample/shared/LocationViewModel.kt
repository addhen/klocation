// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0

package com.addhen.klocation.sample.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.addhen.klocation.LocationService
import com.addhen.klocation.LocationState
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

public class LocationViewModel(private val locationService: LocationService) : ViewModel() {
  private val viewStateEmitter =
    MutableStateFlow(LocationUiState(flag = LocationUiState.Flag.LOADING))

  public val viewState: StateFlow<LocationUiState> = viewStateEmitter
    .stateIn(
      viewModelScope,
      started = SharingStarted.WhileSubscribed(5_000),
      viewStateEmitter.value,
    )

  init {
    locationService.requestLocationUpdates()
      .distinctUntilChanged()
      .onEach { state ->
        Logger.d(LocationViewModel::class.simpleName.toString()) { "state $state" }
        viewStateEmitter.update { currentUiState ->
          currentUiState.copy(
            flag = LocationUiState.Flag.IDLE,
            observeLocationState = state,
          )
        }
      }.launchIn(viewModelScope)
  }

  public fun getLastKnowLocation() {
    viewModelScope.launch {
      try {
        val locationState = locationService.getLastKnownLocation()
        Logger.d(LocationViewModel::class.simpleName ?: "") { "lastKnownLocation $locationState" }
        viewStateEmitter.update { currentUiState ->
          currentUiState.copy(
            flag = LocationUiState.Flag.IDLE,
            lastKnowLocationState = locationState,
          )
        }
      } catch (e: Throwable) {
        ensureActive()
        viewStateEmitter.update { it.copy(flag = LocationUiState.Flag.ERROR) }
      }
    }
  }

  public fun stopLocating(): Unit = locationService.stopRequestingLocationUpdates()

  public data class LocationUiState(
    public val observeLocationState: LocationState = LocationState.CurrentLocation(null),
    public val lastKnowLocationState: LocationState = LocationState.CurrentLocation(null),
    public val flag: Flag = Flag.IDLE,
  ) {
    public enum class Flag {
      LOADING,
      ERROR,
      IDLE,
    }
  }
}
