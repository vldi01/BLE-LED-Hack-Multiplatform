package ui.theme

import androidx.compose.ui.graphics.Color

data class AppColor(val light: Color, val dark: Color = light)
class AppColors(
    transformation: AppColor.() -> Color
) {
    val background: Color = AppColor(Color(0xFF797979)).transformation()
    val onBackground: Color = AppColor(Color(0xFFFFFFFF)).transformation()

    val primary: Color = AppColor(Color(0xFF47A87E)).transformation()
    val onPrimary: Color = AppColor(Color(0xffffffff)).transformation()

    val error: Color = AppColor(Color(0xFFA24949)).transformation()
    val onError: Color = AppColor(Color(0xFFFFFFFF)).transformation()
}