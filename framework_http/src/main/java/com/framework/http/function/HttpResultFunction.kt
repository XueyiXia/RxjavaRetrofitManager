package com.framework.http.function

import com.framework.http.exception.EventException
import com.framework.http.exception.ExceptionEngine
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function

/**
 * 统一异常转换，确保所有错误都转为项目内的统一业务异常。
 */
class HttpResultFunction<T : Any> : Function<Throwable, Observable<T>> {
    override fun apply(throwable: Throwable): Observable<T> {
        val throwableException: EventException = ExceptionEngine.handleException(throwable)
        return Observable.error(throwableException)
    }
}
