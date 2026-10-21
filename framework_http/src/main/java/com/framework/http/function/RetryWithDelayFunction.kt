package com.framework.http.function

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Function
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * 明确的重试策略，避免重用实例导致计数器混乱。
 */
class RetryWithDelayFunction(
    private val maxRetries: Int,
    private val retryDelayMillis: Long
) : Function<Observable<Throwable>, ObservableSource<Any>> {

    private val retryCount = AtomicInteger(0)

    @Throws(Exception::class)
    override fun apply(throwableObservable: Observable<Throwable>): ObservableSource<Any> {
        return throwableObservable.flatMap { throwable ->
            val currentRetry = retryCount.incrementAndGet()
            if (currentRetry <= maxRetries) {
                Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS)
            } else {
                Observable.error(throwable)
            }
        }
    }
}
