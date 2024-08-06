package com.addhen.klocation.sample.android.permission

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.addhen.klocation.sample.android.R
import com.addhen.klocation.sample.shared.ScaffoldSample
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreen() {
  val locationPermissionsState = rememberMultiplePermissionsState(
    listOf(
      android.Manifest.permission.ACCESS_COARSE_LOCATION,
      android.Manifest.permission.ACCESS_FINE_LOCATION,
    )
  )

  ScaffoldSample(title = stringResource(R.string.request_permission)) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      val allPermissionsRevoked =
        locationPermissionsState.permissions.size == locationPermissionsState.revokedPermissions.size
      val promptResId = when {
        allPermissionsRevoked.not() -> {
          // Both location permissions have been denied
          R.string.request_location_permission
        }

        locationPermissionsState.shouldShowRationale -> {
          R.string.request_location_permission_rationale
        }

        else -> {
          // First time the user sees this feature or the user doesn't want to be asked again
          R.string.request_location_permission_rationale
        }
      }

      val buttonTextResId = if (!allPermissionsRevoked) {
        R.string.request_fine_permission
      } else {
        R.string.request_permission
      }

      RequestPermission(promptResId, buttonTextResId) {
        locationPermissionsState.launchMultiplePermissionRequest()
      }
    }
  }
}

@Composable
fun RequestPermission(
  @StringRes promptResId: Int,
  @StringRes buttonTextResId: Int,
  onButtonClick: () -> Unit
) {
  ImageResource(modifier = Modifier.size(80.dp, 80.dp), image = R.drawable.ic_launcher_foreground)
  Spacer(modifier = Modifier.height(16.dp))
  Text(text = stringResource(id = promptResId), textAlign = TextAlign.Center)
  Spacer(modifier = Modifier.height(16.dp))
  Button(onClick = { onButtonClick() }) {
    Text(text = stringResource(id = buttonTextResId))
  }
}

@Composable
private fun ImageResource(
  modifier: Modifier,
  @DrawableRes image: Int,
  imageDescription: String? = null
) {
  Image(
    modifier = modifier,
    painter = painterResource(id = image),
    contentDescription = imageDescription
  )
}
