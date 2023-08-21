package architecture

import com.diachuk.routing.Routing
import core.Scanner
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ui.screens.DeviceRoute
import util.Commands

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
            AppEvent.DeviceScreenClosed -> {
                _state.value.selectedDevice?.disconnect()
            }

            is AppEvent.SendColor -> {
                viewModelScope.launch {
                    _state.value.selectedDevice?.write(Commands.colorCommand(event.color), "FFD9")
                }
            }

            is AppEvent.SendPower -> {
                viewModelScope.launch {
                    _state.value.selectedDevice?.write(Commands.switchOn(event.on), "FFD9")
                }
            }

            is AppEvent.DeviceSelected -> {
                val device = _state.value.discoveredDevices[event.deviceIndex]
                scanner.stopScan()
                device.connect()
                _state.update { it.copy(selectedDevice = device) }
                routing.navigate(DeviceRoute)
            }

            AppEvent.Reconnect -> {
                _state.value.selectedDevice?.disconnect()
                _state.value.selectedDevice?.connect()
            }
        }
    }

    private fun scan() {
        viewModelScope.launch {
            _state.update { it.copy(isScanning = true) }
            scanner.startScan()
            delay(10000)
            scanner.stopScan()
            _state.update { it.copy(isScanning = false) }
        }
    }
}
