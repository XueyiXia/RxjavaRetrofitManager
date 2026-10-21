package com.framework.http.function

import com.framework.http.observer.HttpObserver
import io.reactivex.rxjava3.functions.Action

/**
 * 释放订阅时的观察者引用，避免重复释放导致状态异常。
 */
class OnDisposeAction constructor(private val httpObserver: HttpObserver<Any>?) : Action {
    @Throws(Exception::class)
    override fun run() {
        httpObserver?.dispose()
    }
}
