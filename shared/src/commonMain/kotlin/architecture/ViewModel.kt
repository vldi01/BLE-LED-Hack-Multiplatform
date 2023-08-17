package architecture

import DI
import com.diachuk.routing.Routing
import core.BluetoothDevice
import core.Scanner
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ui.screens.DeviceRoute

class ViewModel(
    private val scanner: Scanner,
    private val routing: Routing
) : BaseViewModel() {

    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()

    init {
        scanner.devices.onEach { devices ->
            _state.update { it.copy(discoveredDevices = devices) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: AppEvent) {
        when (event) {
            AppEvent.ScanClick -> scan()
            AppEvent.DeviceScreenClosed -> {}
            is AppEvent.SendColor -> {}
            is AppEvent.SendPower -> {}
            is AppEvent.DeviceSelected -> {
                _state.update { it.copy(selectedDevice = it.discoveredDevices[event.deviceIndex]) }
                routing.navigate(DeviceRoute)
            }
        }
    }

    private fun scan() {
        viewModelScope.launch {
            _state.update { it.copy(isScanning = true, discoveredDevices = emptyList()) }
            scanner.startScan()
            delay(10000)
            scanner.stopScan()
            _state.update { it.copy(isScanning = false) }
        }
    }
}
