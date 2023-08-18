package architecture

import androidx.compose.ui.graphics.Color

sealed interface AppEvent {
    data object ScanClick: AppEvent
    data object Reconnect: AppEvent
    data class SendColor(val color: Color): AppEvent
    data class SendPower(val on: Boolean): AppEvent
    data object DeviceScreenClosed: AppEvent
    data class DeviceSelected(val deviceIndex: Int): AppEvent
}