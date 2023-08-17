package ui.screens

import DI
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
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
        Button(onClick = { pushEvent(AppEvent.ScanClick) }) {
            androidx.compose.material3.Text(text = "Scan")
        }

        LazyColumn {
            items(state.discoveredDevices) {device->
                Column(
                    Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .clickable {
                            println("device = ${device}")
                        }
                        .padding(8.dp)
                ) {
                    Text(text = device.name, style = MaterialTheme.typography.headlineMedium)
                    Text(text = device.id)
                }
            }
        }
    }
}