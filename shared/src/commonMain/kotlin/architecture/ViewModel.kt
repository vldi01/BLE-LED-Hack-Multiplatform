package architecture

import DI
import core.BluetoothDevice
import core.Scanner
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModel(
    private val scanner: Scanner
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
