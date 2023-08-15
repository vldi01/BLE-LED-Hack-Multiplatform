package com.diachuk.routing

import androidx.compose.runtime.Composable

@Composable
inline fun <reified T> withViewState(block: @Composable T.() -> Unit) {
    val state = LocalViewState.current
    if (state is T) {
        state.block()
    }
}