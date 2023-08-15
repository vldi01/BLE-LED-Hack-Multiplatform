package com.diachuk.routing.routes

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import com.diachuk.routing.NavArgsHolder

interface Route<NavArgs> {
    val enterTransition: EnterTransition
    val exitTransition: ExitTransition
    val name: String
    val viewState: () -> Any

    @Composable
    fun Content(argsHolder: NavArgsHolder<out Any?>?)
}
