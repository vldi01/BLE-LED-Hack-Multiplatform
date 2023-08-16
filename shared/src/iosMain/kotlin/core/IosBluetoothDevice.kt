package core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.darwin.NSObject

class IosBluetoothDevice: NSObject(), CBPeripheralDelegateProtocol, BluetoothDevice {
    private val _state = MutableStateFlow(BluetoothDeviceState.NotConnected)
    override val state: StateFlow<BluetoothDeviceState> = _state.asStateFlow()

    private val _characteristics = mutableListOf<DeviceCharacteristic>()
    override val characteristics: List<DeviceCharacteristic> = _characteristics.toList()

    override fun connect() {
        TODO("Not yet implemented")
    }

    override fun disconnect() {
        TODO("Not yet implemented")
    }
}