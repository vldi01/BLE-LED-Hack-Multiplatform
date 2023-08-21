package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import com.diachuk.routing.Routing
import com.diachuk.routing.RoutingHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.koin.compose.koinInject
import ui.screens.ScanRoute
import ui.theme.AppTheme

@Composable
fun App() {
    val routing = koinInject<Routing>()
    val scope = koinInject<CoroutineScope>()

    DisposableEffect(Unit) {
        onDispose {
            scope.cancel()
        }
    }

    LaunchedEffect(Unit) {
        routing.changeCurrent(ScanRoute)
    }

    AppTheme {
        RoutingHost(routing = routing)
    }
}
