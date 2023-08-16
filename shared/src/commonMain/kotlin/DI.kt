import core.Scanner
import architecture.ViewModel

object DI {
    val scanner = Scanner()
    val viewModel = ViewModel(scanner)
}
