// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.klocation

public sealed interface LocationState {
  public data object PermissionMissing : LocationState
  public data object LocationDisabled : LocationState
  public data object NoNetworkEnabled : LocationState
  public data class CurrentLocation<out T>(val location: T?) : LocationState
  public data class Error(val cause: Throwable) : LocationState
}
