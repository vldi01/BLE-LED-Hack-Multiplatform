package ui.components

import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle
import platform.UIKit.UISelectionFeedbackGenerator

actual class BHapticFeedback: HapticFeedback {
    private val feedbackGenerator = UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleSoft)

    override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) {
        feedbackGenerator.impactOccurred()
    }
}