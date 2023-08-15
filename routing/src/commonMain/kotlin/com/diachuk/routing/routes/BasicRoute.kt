package com.diachuk.routing.routes

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import com.diachuk.routing.NavArgsHolder
import kotlin.random.Random

abstract class BasicRoute<T>(
    override val viewState: () -> Any,
    override val enterTransition: EnterTransition,
    override val exitTransition: ExitTransition,
) : Route<T> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Route<*>) return false

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

fun createRoute(
    uniqueName: String = Random.nextLong().toString(),
    viewState: () -> Any = {},
    enterTransition: EnterTransition = fadeIn(),
    exitTransition: ExitTransition = fadeOut(),
    content: @Composable () -> Unit
) = object : BasicRoute<Unit>(viewState, enterTransition, exitTransition) {
    override val name = uniqueName

    @Composable
    override fun Content(argsHolder: NavArgsHolder<out Any?>?) {
        content()
    }
}

fun <T> createRoute(
    uniqueName: String = Random.nextLong().toString(),
    viewState: () -> Any = {},
    enterTransition: EnterTransition = fadeIn(),
    exitTransition: ExitTransition = fadeOut(),
    content: @Composable (NavArgsHolder<T>?) -> Unit
) = object : BasicRoute<T>(viewState, enterTransition, exitTransition) {
    override val name = uniqueName

    @Suppress("UNCHECKED_CAST")
    @Composable
    override fun Content(argsHolder: NavArgsHolder<out Any?>?) {
        val args = argsHolder as NavArgsHolder<T>?
        content(args)
    }
}
