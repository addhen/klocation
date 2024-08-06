package com.addhen.klocation.sample.android

import android.app.Application
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LocationViewModel(
  private val locationService: LocationService
): ViewModel() {
  private val viewStateEmitter = MutableStateFlow<LocationState>(LocationState.CurrentLocation<Location>(null))

  val viewState: StateFlow<LocationState> = viewStateEmitter
    .stateIn(
      viewModelScope,
      started = SharingStarted.WhileSubscribed(5_000),
      viewStateEmitter.value
    )

  init {
    locationService.observeLocationUpdates().onEach { state ->
      Log.d(LocationViewModel::class.simpleName, "state $state")
      viewStateEmitter.emit(state)
    }.launchIn(viewModelScope)
  }

  fun getLastKnowLocation() {
    viewModelScope.launch {
      try {
        viewStateEmitter.emit(locationService.getLastKnownLocation())
      } catch (e: Throwable) {
        if (e is CancellationException) throw e
        viewStateEmitter.emit(LocationState.Error(""))
      }
    }
  }
}

@Suppress("UNCHECKED_CAST")
class LocationViewModelFactory(
  private val locationService: LocationService
) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return LocationViewModel(locationService) as T
  }
}
