package core

enum class Platform {
    Android, Ios
}

fun Platform.isAndroid() = this == Platform.Android
fun Platform.isIos() = this == Platform.Ios

expect val platform: Platform