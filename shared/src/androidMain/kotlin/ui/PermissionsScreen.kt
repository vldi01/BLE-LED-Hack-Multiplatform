package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.PermissionsManager
import ui.components.BSpacer

@Composable
fun PermissionsScreen(permissionsManager: PermissionsManager) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Some permissions are not granted. Click button or grant in in preferences")

        BSpacer(24.dp)

        Button({
            permissionsManager.requestPermissions()
        }) {
            Text("Request permissions")
        }
    }
}