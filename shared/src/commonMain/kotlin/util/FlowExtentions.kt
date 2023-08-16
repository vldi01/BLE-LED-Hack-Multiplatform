package util

import architecture.AppState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

fun <T> Flow<AppState>.addSource(flow: Flow<T>, block: AppState.(T) -> AppState): Flow<AppState> {
    return combine(flow) { state, t ->
        state.block(t)
    }
}
