package core

import android.Manifest
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

actual class Scanner(
    private val context: Context,
    private val scope: CoroutineScope
) {
    private val _devices = MutableStateFlow(emptyList<BluetoothDevice>())
    actual val devices: StateFlow<List<BluetoothDevice>> = _devices.asStateFlow()


    private val callback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            val device = result?.device ?: return
            val deviceName =
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    device.name ?: ""
                } else {
                    ""
                }

            if (deviceName.contains("Triones")) {
                _devices.tryEmit(_devices.value + BluetoothDevice(device))
            }
        }

        override fun onScanFailed(errorCode: Int) {
            println("errorCode = ${errorCode}")
        }
    }

    actual fun startScan() {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

        scope.launch(Dispatchers.Default) {
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
    }
}