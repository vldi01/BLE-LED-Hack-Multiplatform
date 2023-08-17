package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ui.shadow.zHeight
import ui.theme.AppTheme
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

@Composable
fun BSpinner(
    angle: Float,
    onAngleChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    vibrationEveryDegree: Float = 5f,
    color: Color = AppTheme.colors.background,
) {
    var lastAngle by remember { mutableStateOf(0f) }
    var size by remember { mutableStateOf(IntSize(0, 0)) }

    var change by remember { mutableStateOf(0f) }
    val haptic = LocalHapticFeedback.current

    fun calculateAngle(position: Offset): Float {
        val center = size / 2
        return atan2(position.y - center.height, position.x - center.width) / PI.toFloat() * 180
    }

    Box(
        modifier = modifier
            .zHeight(
                shape = CircleShape,
                height = 6.dp
            )
            .background(color)
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        lastAngle = calculateAngle(it)
                    },
                    onDrag = { pointer, _ ->
                        val newAngle = calculateAngle(pointer.position)
                        val difference = calculateAngleDifference(lastAngle, newAngle)
                        onAngleChanged(difference)
                        change += difference
                        lastAngle = newAngle

                        if (abs(change) >= vibrationEveryDegree) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            change = 0f
                        }
                    }
                )
            }
            .graphicsLayer { rotationZ = angle }
            .padding(top = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .graphicsLayer { rotationZ = -angle / 2 }
                .size(12.dp)
                .zHeight(
                    shape = CircleShape,
                    height = (-1.2).dp
                )
                .background(color)
        )
    }
}

fun calculateAngleDifference(angleA: Float, angleB: Float): Float {
    val fullCircle = 360f
    val halfCircle = fullCircle / 2

    val rawDifference = (angleB - angleA + halfCircle) % fullCircle - halfCircle
    return if (rawDifference < -halfCircle) {
        rawDifference + fullCircle
    } else {
        rawDifference
    }
}
