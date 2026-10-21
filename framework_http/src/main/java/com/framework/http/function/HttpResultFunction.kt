package com.framework.http.function

import com.framework.http.exception.EventException
import com.framework.http.exception.ExceptionEngine
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Function

/**
 * 统一异常转换：返回业务层的 EventException，避免泛型和 Observable 类型错位。
 */
class HttpResultFunction<T : Any> : Function<Throwable, ObservableSource<T>> {
    override fun apply(throwable: Throwable): ObservableSource<T> {
        val throwableException: EventException = ExceptionEngine.handleException(throwable)
        return Observable.error(throwableException)
    }
}
