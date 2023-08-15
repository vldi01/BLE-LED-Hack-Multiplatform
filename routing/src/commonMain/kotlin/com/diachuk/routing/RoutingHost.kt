package com.diachuk.routing

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.diachuk.routing.routes.EmptyRoute
import com.diachuk.routing.routes.Route
import kotlinx.coroutines.flow.Flow


val LocalRouting = compositionLocalOf { Routing(EmptyRoute) }
val LocalViewState = compositionLocalOf<Any> { }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RoutingHost(
    modifier: Modifier = Modifier,
    routing: Routing = remember { Routing(EmptyRoute) }
) {
    CompositionLocalProvider(LocalRouting provides routing) {
        BackHandler(routing.backStack.size > 0) {
            routing.navigateBack()
        }
        val targetState = routing.currentRoute.collectAsState()

        AnimatedContent(
            targetState = targetState.value,
            modifier = modifier,
            transitionSpec = {
                (routing.forceEnterTransition ?: targetState.value.route.enterTransition) with
                        (routing.forceExitTransition ?: initialState.route.exitTransition)
            }

        ) { routeProvider ->
            CompositionLocalProvider(LocalViewState provides routeProvider.viewState) {
                routeProvider.route.Content(routeProvider.navArgs)
            }
        }
    }
}

@Composable
fun RoutingHost(
    modifier: Modifier = Modifier,
    startRoute: Route<*>
) {
    RoutingHost(
        modifier,
        routing = remember {
            Routing(startRoute)
        }
    )
}

@Composable
fun RoutingHost(
    modifier: Modifier = Modifier,
    start: Route<Unit> = EmptyRoute,
    flowRoute: Flow<Route<Unit>>,
    behaviour: NavigationBehaviour
) {
    RoutingHost(
        modifier,
        routing = remember {
            Routing(start, flowRoute, behaviour)
        }
    )
}
