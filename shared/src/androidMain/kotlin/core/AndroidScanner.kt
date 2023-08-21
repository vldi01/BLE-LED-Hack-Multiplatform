package core

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
actual class Scanner(
    private val context: Context,
    private val scope: CoroutineScope
) {
    private val _devices = MutableStateFlow(setOf<BluetoothDevice>())
    actual val devices: Flow<List<BluetoothDevice>> = _devices.map { it.toList() }

    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter
    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

    private val callback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            val device = result?.device ?: return

            if(device.name != null) {
                scope.launch {
                    _devices.emit(_devices.value + BluetoothDevice(device))
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            println("errorCode = ${errorCode}")
        }
    }

    actual fun startScan() {
        scope.launch(Dispatchers.Default) {
            _devices.emit(setOf())
            launch {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
                    bluetoothLeScanner.startScan(callback)
                }
            }
            delay(10000)
            bluetoothLeScanner.stopScan(callback)
        }
    }

    actual fun stopScan() {
        bluetoothLeScanner.stopScan(callback)
    }
}