package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

val LocalColors = compositionLocalOf { AppColors { light } }

object AppTheme {
    val colors: AppColors
        @Composable get() = LocalColors.current
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) AppColors { dark } else AppColors { light }

    CompositionLocalProvider(LocalColors provides colorScheme) {
        MaterialTheme(
            colorScheme = lightColorScheme(
                primary = colorScheme.primary,
                onPrimary = colorScheme.onPrimary,
                background = colorScheme.background,
                onBackground = colorScheme.onBackground,
                surface = colorScheme.background,
                onSurface = colorScheme.onBackground,
                error = colorScheme.error,
                onError = colorScheme.onError,
            ),
            typography = Typography,
            content = content
        )
    }
}