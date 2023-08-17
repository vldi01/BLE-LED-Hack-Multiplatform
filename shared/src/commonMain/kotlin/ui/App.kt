package ui

import DI
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.diachuk.routing.RoutingHost
import ui.screens.ScanRoute
import ui.theme.AppTheme

@Composable
fun App() {
    val routing = DI.routing

    LaunchedEffect(Unit) {
        routing.changeCurrent(ScanRoute)
    }

    AppTheme {
        RoutingHost(routing = routing)
    }
}
