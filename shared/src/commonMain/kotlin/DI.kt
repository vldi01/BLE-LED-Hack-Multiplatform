import architecture.ViewModel
import com.diachuk.routing.Routing
import core.Scanner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

object DI {
    private val scanner = Scanner()
    val routing = Routing()
    val scope = CoroutineScope(Job())
    val viewModel = ViewModel(scanner, routing)
}
