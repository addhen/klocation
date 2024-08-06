// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

sealed interface LocationState {
  data object PermissionMissing : LocationState
  data object LocationDisabled : LocationState
  data object NoNetworkEnabled : LocationState
  data class CurrentLocation<out T>(val location: T?) : LocationState
  data class Error(val cause: Throwable) : LocationState
}
