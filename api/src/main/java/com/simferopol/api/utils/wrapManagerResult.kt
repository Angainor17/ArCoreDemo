package com.simferopol.api.utils

suspend fun <T> wrapManagerResult(getter: suspend () -> T): ManagerResult<T> {
    return try {
        ManagerResult(getter())
    } catch (e: Throwable) {
        ManagerResult(error = e.message)
    }
}