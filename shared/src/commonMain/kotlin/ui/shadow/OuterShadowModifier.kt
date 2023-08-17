package ui.shadow

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb

@Suppress("NAME_SHADOWING")
fun Modifier.outerShadow(
    shape: Shape,
    clip: Boolean = true,
    vararg shadows: ShadowStyle? = arrayOf(null),
): Modifier = composed {
    val shadows = shadows
        .mapNotNull { it ?: LocalShadowStyle.current }

    this
        .drawBehind {
            shadows.forEach { style ->
                drawIntoCanvas {
                    val paint = Paint()
                    val frameworkPaint = paint.asFrameworkPaint()
                    frameworkPaint.color = style.color.toArgb()

                    val xPx = style.x.toPx()
                    val yPx = style.y.toPx()

//                    frameworkPaint.setShadowLayer(
//                        style.blurRadius.toPx(),
//                        xPx,
//                        yPx,
//                        style.color.toArgb()
//                    )

                    val spreadPx = style.spread.toPx()
                    val outline = shape.createOutline(
                        size = size.copy(
                            width = size.width + spreadPx,
                            height = size.height + spreadPx
                        ),
                        layoutDirection = layoutDirection,
                        density = this
                    )
                    it.drawOutline(outline, paint)
                }
            }
        }
        .graphicsLayer(shape = shape, clip = true)
}

fun Modifier.outerShadow(
    vararg shadows: ShadowStyle? = arrayOf(null),
): Modifier = outerShadow(shape = RectangleShape, clip = true, shadows = shadows)

fun Modifier.outerShadow(
    shape: Shape,
    vararg shadows: ShadowStyle? = arrayOf(null),
): Modifier = outerShadow(shape = shape, clip = true, shadows = shadows)
