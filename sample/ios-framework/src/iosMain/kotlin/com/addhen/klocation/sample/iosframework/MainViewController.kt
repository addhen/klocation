import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.rememberNavController
import com.addhen.klocation.LocationService
import com.addhen.klocation.sample.iosframework.LocationScreen
import com.addhen.klocation.sample.iosframework.LocationViewModel
import com.addhen.klocation.sample.iosframework.SampleApp
import com.addhen.klocation.sample.iosframework.navigation.LocationRoute
import com.addhen.klocation.sample.iosframework.navigation.buildNavOptions
import com.addhen.klocation.sample.iosframework.permission.LocationPermissionScreen
import com.addhen.klocation.sample.iosframework.permission.LocationPermissionViewModel
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.ios.PermissionsController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocation
import platform.UIKit.UIViewController

@OptIn(ExperimentalForeignApi::class)
public fun MainViewController(): UIViewController = ComposeUIViewController {
  val navController = rememberNavController()
  SampleApp(
    navController = navController,
    permissionsController = PermissionsController(),
    permissionScreen = {
      val viewModel = LocationPermissionViewModel(
        PermissionsController(),
        Permission.LOCATION,
      )
      LocationPermissionScreen(viewModel) {
        navController.navigate(LocationRoute, navController.buildNavOptions())
      }
    },
    locationScreen = {
      val locationService = LocationService()
      val viewModel = LocationViewModel(LocationService())
      LocationScreen(viewModel, locationService) { locationState ->
        val location = locationState.location as? CLLocation
        location?.coordinate?.useContents {
          "$latitude,$longitude"
        } ?: ""
      }
    }
  )
}
