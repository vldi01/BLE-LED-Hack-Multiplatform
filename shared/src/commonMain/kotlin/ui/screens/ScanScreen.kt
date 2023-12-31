package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import architecture.AppEvent
import architecture.AppState
import architecture.ViewModel
import com.diachuk.routing.routes.createRoute
import org.koin.compose.koinInject

val ScanRoute = createRoute {
    val vm = koinInject<ViewModel>()
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
            itemsIndexed(state.discoveredDevices) { i, device ->
                Column(
                    Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .clickable {
                            pushEvent(AppEvent.DeviceSelected(i))
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
