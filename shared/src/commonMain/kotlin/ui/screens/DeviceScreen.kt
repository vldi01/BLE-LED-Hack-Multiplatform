package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import architecture.AppEvent
import architecture.AppState
import com.diachuk.routing.LocalRouting
import com.diachuk.routing.routes.createRoute
import ui.App
import ui.components.BSpacer
import ui.components.BSpinner
import ui.shadow.zHeight
import ui.theme.AppTheme

val DeviceRoute = createRoute {
    val vm = DI.viewModel
    DeviceScreenUi(
        vm.state.collectAsState().value
    ) { vm.onEvent(it) }
}

@Composable
fun DeviceScreenUi(state: AppState, pushEvent: (AppEvent) -> Unit) {
    state.selectedDevice ?: return
    val routing = LocalRouting.current
    var color by remember { mutableStateOf(0f) }
    var brightness by remember { mutableStateOf(1f) }

    val resultColor = { Color.hsv(color, 1f, brightness) }
    var lasColor by remember { mutableStateOf(resultColor()) }

    val sendColor = remember {
        {
            if (resultColor() != lasColor) {
                lasColor = resultColor()
                pushEvent(AppEvent.SendColor(lasColor))
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            pushEvent(AppEvent.DeviceScreenClosed)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        Button({
            routing.navigateBack()
        }){
            Text("Go back")
        }
        Text(text = state.selectedDevice.name)
        Text(text = "Connections state: ${state.selectedDevice.state.collectAsState().value}")

        BSpacer(24.dp)

        Box(
            modifier = Modifier
                .size(24.dp)
                .zHeight(CircleShape, (-2).dp)
                .background(resultColor())
                .align(Alignment.CenterHorizontally)
        )

        BSpacer(24.dp)

        Row(Modifier.align(Alignment.CenterHorizontally)) {
            Button(onClick = { pushEvent(AppEvent.SendPower(true)) }) {
                Text(text = "ON")
            }
            Button(onClick = { pushEvent(AppEvent.SendPower(false)) }) {
                Text(text = "OFF")
            }
        }

        BSpacer(24.dp)

        Row(Modifier.align(Alignment.CenterHorizontally)) {
            BSpinner(
                modifier = Modifier.size(100.dp),
                angle = color,
                onAngleChanged = {
                    color = ((color + it) + 360) % 360
                    sendColor()
                }
            )
            BSpacer(24.dp)
            BSpinner(modifier = Modifier.size(100.dp), angle = brightness * 360, onAngleChanged = {
                brightness = (brightness + it / 360).coerceIn(0f, 1f)
                sendColor()
            })
        }
    }
}
