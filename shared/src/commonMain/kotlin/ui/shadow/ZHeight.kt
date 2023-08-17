package ui.shadow

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import ui.shadow.ShadowStyle
import ui.shadow.innerShadow
import ui.shadow.outerShadow

fun Modifier.zHeight(
    shape: Shape,
    height: Dp
) = if (height.value > 0) {
    this.outerShadow(
        shape = shape,
        ShadowStyle(
            x = height,
            y = height,
            blurRadius = height * 2,
            color = Color(0x40000000)
        ),
        ShadowStyle(
            x = -height,
            y = -height,
            blurRadius = height * 2,
            color = Color(0x40FFFFFF)
        )
    )
} else if (height.value < 0) {
    this.innerShadow(
        shape = shape,
        ShadowStyle(
            x = height,
            y = height,
            blurRadius = -height * 2,
            color = Color(0x40FFFFFF)
        ),
        ShadowStyle(
            x = -height,
            y = -height,
            blurRadius = -height * 2,
            color = Color(0x40000000)
        )
    )
} else {
    this
}