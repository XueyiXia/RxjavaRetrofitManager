package com.framework.http.function

import com.framework.http.observer.HttpObserver
import io.reactivex.rxjava3.functions.Action

/**
 * 释放订阅时保留的观察者引用。
 */
class OnDisposeAction<T : Any>(
    private val httpObserver: HttpObserver<T>?
) : Action {

    @Throws(Exception::class)
    override fun run() {
        httpObserver?.dispose()
    }
}
