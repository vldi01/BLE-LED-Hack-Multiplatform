import architecture.ViewModel
import com.diachuk.routing.Routing
import core.Scanner

object DI {
    private val scanner = Scanner()
    val routing = Routing()
    val viewModel = ViewModel(scanner, routing)
}
