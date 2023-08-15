package com.diachuk.routing

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(enabled: Boolean, block: () -> Unit)
