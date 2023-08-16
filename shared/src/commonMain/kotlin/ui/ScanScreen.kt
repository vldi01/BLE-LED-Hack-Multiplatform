package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import architecture.AppEvent
import architecture.AppState

@Composable
fun ScanScreen() {
    val vm = remember { DI.viewModel }
    ScanScreenUi(vm.state.collectAsState().value) { vm.onEvent(it) }
}

@Composable
fun ScanScreenUi(
    state: AppState,
    pushEvent: (AppEvent) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Text("Scanning: ${state.isScanning}")
        Button(
            onClick = {
                pushEvent(AppEvent.ScanClick)
            }
        ) {
            Text("Scan")
        }
    }
}