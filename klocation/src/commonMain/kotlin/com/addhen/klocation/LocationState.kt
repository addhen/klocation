package com.addhen.klocation

sealed interface LocationState {
  data object PermissionMissing : LocationState
  data object LocationDisabled : LocationState
  data object NoNetworkEnabled : LocationState
  data class CurrentLocation(val point: Point) : LocationState
  data class LastKnownLocation(val point: Point) : LocationState
  data class Error(val message: String) : LocationState
}
