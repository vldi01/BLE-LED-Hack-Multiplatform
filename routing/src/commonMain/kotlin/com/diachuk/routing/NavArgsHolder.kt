package com.diachuk.routing

import androidx.compose.runtime.Stable

@Stable
class NavArgsHolder<T>(private val args: T? = null) {
    private var isProvided = false

    val peek: T? get() = args

    val provideArgs: T?
        get() {
            if (!isProvided) {
                isProvided = true
                return args
            }
            return null
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as NavArgsHolder<*>

        return args == other.args
    }

    override fun hashCode(): Int {
        return args?.hashCode() ?: 0
    }
}