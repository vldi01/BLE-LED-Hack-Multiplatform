package ui.components


import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun RowScope.BSpacer(
    width: Dp
) {
    Spacer(modifier = Modifier.width(width))
}

@Composable
fun ColumnScope.BSpacer(
    height: Dp
) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun RowScope.BSpacer() {
    Spacer(modifier = Modifier.weight(1f))
}

@Composable
fun ColumnScope.BSpacer() {
    Spacer(modifier = Modifier.weight(1f))
}
