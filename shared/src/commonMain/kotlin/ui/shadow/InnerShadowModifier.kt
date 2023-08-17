package ui.shadow

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import kotlin.math.abs
import kotlin.math.max

@Suppress("NAME_SHADOWING")
fun Modifier.innerShadow(
    shape: Shape = RectangleShape,
    clip: Boolean = true,
    vararg shadows: ShadowStyle? = arrayOf(null),
): Modifier = composed {
    val shadows = shadows
        .mapNotNull { it ?: LocalShadowStyle.current }

    this@innerShadow
        .graphicsLayer(shape = shape, clip = clip)
        .drawWithContent {
            drawContent()
            shadows.forEach { style ->
                val paint = Paint()
                val frameworkPaint = paint.asFrameworkPaint()
                frameworkPaint.color = style.color.toArgb()

                val xPx = style.x.toPx()
                val yPx = style.y.toPx()
                val strokeWidth = max(abs(xPx), abs(yPx))*3

                paint.style = PaintingStyle.Stroke
                paint.strokeWidth = strokeWidth

//                frameworkPaint.setShadowLayer(
//                    style.blurRadius.toPx(),
//                    xPx,
//                    yPx,
//                    style.color.toArgb()
//                )

                val spreadPx = style.spread.toPx()
                val outline = shape.createOutline(
                    size = size.copy(
                        width = size.width + spreadPx + strokeWidth,
                        height = size.height + spreadPx + strokeWidth
                    ),
                    layoutDirection = layoutDirection,
                    density = this
                )

                drawContext.canvas.run {
                    translate(-strokeWidth/2, -strokeWidth/2) {
                        drawOutline(outline, paint)
                    }
                }
            }
        }
}

fun Modifier.innerShadow(
    vararg shadows: ShadowStyle? = arrayOf(null),
): Modifier = innerShadow(shape = RectangleShape, clip = true, shadows = shadows)


fun Modifier.innerShadow(
    shape: Shape,
    vararg shadows: ShadowStyle? = arrayOf(null),
): Modifier = innerShadow(shape = shape, clip = true, shadows = shadows)
