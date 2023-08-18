package ui

import DI
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import com.diachuk.routing.RoutingHost
import kotlinx.coroutines.cancel
import ui.screens.ScanRoute
import ui.theme.AppTheme

@Composable
fun App() {
    val routing = DI.routing

    DisposableEffect(Unit) {
        onDispose {
            DI.scope.cancel()
        }
    }

    LaunchedEffect(Unit) {
        routing.changeCurrent(ScanRoute)
    }

    AppTheme {
        RoutingHost(routing = routing)
    }
}
