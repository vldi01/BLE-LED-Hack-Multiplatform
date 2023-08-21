package core

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class PermissionsManagerState {
    Granted, NotGranted
}

class PermissionsManager(private val activity: ComponentActivity) {
    private val _state = MutableStateFlow(PermissionsManagerState.NotGranted)
    val state = _state.asStateFlow()

    private val requiredPermissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    @SuppressLint("InlinedApi")
    private val requiredPermissionsApi31 = arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT
    )

    private val requestPermissionLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            checkForPermissions()
        }

    fun requestPermissions() {
        var permissions = requiredPermissions
        if (VERSION.SDK_INT >= VERSION_CODES.S) {
            permissions += requiredPermissionsApi31
        }
        if (_state.value == PermissionsManagerState.NotGranted) {
            requestPermissionLauncher.launch(requiredPermissions)
        }
    }

    fun checkForPermissions() {
        val granted = requiredPermissions.all {
            ContextCompat.checkSelfPermission(
                activity,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
        val grantedApi31 = VERSION.SDK_INT < VERSION_CODES.S || (
                requiredPermissionsApi31.all {
                    ContextCompat.checkSelfPermission(
                        activity,
                        it
                    ) == PackageManager.PERMISSION_GRANTED
                })

        _state.update { if (granted && grantedApi31) PermissionsManagerState.Granted else PermissionsManagerState.NotGranted }
    }
}