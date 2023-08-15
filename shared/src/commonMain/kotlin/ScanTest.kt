object DI {
    val scanTest = ScanTest()
}

expect class ScanTest() {
    fun printScan()
}