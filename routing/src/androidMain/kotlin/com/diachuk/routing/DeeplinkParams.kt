package com.diachuk.routing

data class DeeplinkParams(
    val scheme: String,
    val host: String,
    val path: String,
    val params: Map<String, String>
) {
    fun match(scheme: String? = null, host: String? = null, path: String? = null, paramNames: List<String>? = null): Boolean {
        return when {
            scheme != null && scheme != this.scheme -> false
            host != null && host != this.host -> false
            path != null && path != this.path -> false
            paramNames != null && !params.keys.containsAll(paramNames) -> false
            else -> true
        }
    }
}
