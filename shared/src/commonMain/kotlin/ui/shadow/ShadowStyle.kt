package ui.shadow

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ShadowStyle(
    val x: Dp = 3.dp,
    val y: Dp = 3.dp,
    val blurRadius: Dp = 10.dp,
    val color: Color = Color.Red,
    val spread: Dp = 0.dp,
)

val LocalShadowStyle = compositionLocalOf { ShadowStyle() }
