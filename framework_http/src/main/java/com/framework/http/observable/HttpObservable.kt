package com.framework.http.observable

import com.framework.http.function.HttpResultFunction
import com.framework.http.function.OnDisposeAction
import com.framework.http.function.RetryWithDelayFunction
import com.framework.http.observer.HttpObserver
import com.framework.http.scheduler.SchedulerUtils
import io.reactivex.rxjava3.core.Observable

/**
 * 网络请求被观察者。
 * 修正泛型链路，统一处理 map / retry / dispose / scheduler，避免 T 泛型错位。
 */
class HttpObservable<T : Any> constructor(
    private val apiObservable: Observable<T>?,
    private val httpObserver: HttpObserver<T>?
) {

    companion object {
        var maxRetries = 5
        var retryDelayMillis = 100L
    }

    private fun map(): Observable<T>? {
        return apiObservable?.map { it }
    }

    private fun onErrorResumeNext(): Observable<T>? {
        val httpResultFunction = HttpResultFunction<T>()
        return map()?.onErrorResumeNext(httpResultFunction)
    }

    private fun doOnDispose(): Observable<T>? {
        val onDisposeAction = OnDisposeAction(httpObserver)
        return onErrorResumeNext()?.doOnDispose(onDisposeAction)
    }

    fun observe() {
        if (httpObserver != null) {
            val retryWithDelayFunction = RetryWithDelayFunction(maxRetries, retryDelayMillis)
            doOnDispose()
                ?.retryWhen(retryWithDelayFunction)
                ?.compose(SchedulerUtils.ioToMainScheduler())
                ?.subscribe(httpObserver)
        }
    }
}
