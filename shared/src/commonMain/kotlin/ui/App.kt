package ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import ui.screens.ScanScreen
import ui.theme.AppTheme

@Composable
fun App() {
    AppTheme {
        ScanScreen()
    }
}
