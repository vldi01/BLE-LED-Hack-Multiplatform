package com.diachuk.routing

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.mutableStateListOf
import com.diachuk.routing.routes.EmptyRoute
import com.diachuk.routing.routes.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RouteProvider<NavArgs>(
    val route: Route<NavArgs>,
    val viewState: Any = Unit,
    val navArgs: NavArgsHolder<NavArgs>? = null
)

class Routing(start: Route<*> = EmptyRoute) {
    private val _currentRoute = MutableStateFlow(RouteProvider(start))
    val currentRoute = _currentRoute.asStateFlow()

    val backStack = mutableStateListOf<RouteProvider<*>>()
    var forceExitTransition: ExitTransition? = null
    var forceEnterTransition: EnterTransition? = null
    val prevRoute get() = backStack.lastOrNull()

    constructor(start: Route<Unit>, flowRoute: Flow<Route<Unit>>, behaviour: NavigationBehaviour) : this(start) {
        CoroutineScope(Dispatchers.Default).launch {
            flowRoute.collect { screen ->
                when (behaviour) {
                    NavigationBehaviour.Navigate -> navigate(screen)
                    NavigationBehaviour.ChangeCurrent -> changeCurrent(screen)
                    NavigationBehaviour.NavigateWithoutRepeating -> navigateWithoutRepeating(screen)
                }
            }
        }
    }

    fun changeCurrent(
        nextRoute: Route<Unit>,
        forceExitTransition: ExitTransition? = null,
        forceEnterTransition: EnterTransition? = null
    ) {
        this.forceEnterTransition = forceEnterTransition
        this.forceExitTransition = forceExitTransition

        _currentRoute.value = RouteProvider(route = nextRoute, viewState = nextRoute.viewState())
    }

    fun <T> changeCurrent(
        nextRoute: Route<T>,
        navArgs: T,
        forceExitTransition: ExitTransition? = null,
        forceEnterTransition: EnterTransition? = null
    ) {
        this.forceEnterTransition = forceEnterTransition
        this.forceExitTransition = forceExitTransition

        _currentRoute.value = RouteProvider(route = nextRoute, navArgs = NavArgsHolder(navArgs), viewState = nextRoute.viewState())
    }

    fun <T> navigate(
        nextRoute: Route<T>,
        navArgs: T,
        forceExitTransition: ExitTransition? = null,
        forceEnterTransition: EnterTransition? = null
    ) {
        backStack.add(_currentRoute.value)
        changeCurrent(nextRoute, navArgs, forceExitTransition, forceEnterTransition)
    }

    fun navigate(
        nextRoute: Route<Unit>,
        forceExitTransition: ExitTransition? = null,
        forceEnterTransition: EnterTransition? = null
    ) {
        backStack.add(_currentRoute.value)
        changeCurrent(nextRoute, forceExitTransition, forceEnterTransition)
    }

    fun navigateWithoutRepeating(
        nextRoute: Route<Unit>,
        forceExitTransition: ExitTransition? = null,
        forceEnterTransition: EnterTransition? = null
    ) {
        backStack.removeAll { it == nextRoute }
        backStack.add(_currentRoute.value)
        changeCurrent(nextRoute, forceExitTransition, forceEnterTransition)
    }

    fun navigateBack(
        forceExitTransition: ExitTransition? = null,
        forceEnterTransition: EnterTransition? = null
    ) {
        prevRoute?.let {
            this.forceEnterTransition = forceEnterTransition
            this.forceExitTransition = forceExitTransition

            _currentRoute.value = it
            backStack.removeLast()
        }
    }
}

enum class NavigationBehaviour {
    Navigate, ChangeCurrent, NavigateWithoutRepeating
}