package architecture

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren

abstract class BaseViewModel {
    protected val viewModelScope = CoroutineScope(Job()+Dispatchers.Default)

    fun onStart() {}
    fun onClose() {
        viewModelScope.coroutineContext.cancelChildren()
    }
}