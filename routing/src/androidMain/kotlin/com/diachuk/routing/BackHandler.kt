package com.diachuk.routing

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, block: () -> Unit) {
    androidx.activity.compose.BackHandler(enabled, block)
}
