import core.Scanner

object DI {
    val scanTest = ScanTest()
}

expect object MDI {
    val scanner: Scanner
}