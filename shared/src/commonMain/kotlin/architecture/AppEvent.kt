package architecture

sealed interface AppEvent {
    data object ScanClick: AppEvent
}