package com.framework.http.scheduler

import io.reactivex.rxjava3.core.*
import org.reactivestreams.Publisher

/**
 * 统一调度器基类：统一处理 subscribeOn / observeOn，避免重复样板代码。
 * 让各具体调度器仅保留自身的线程选择逻辑，职责更清晰。
 */
abstract class BaseScheduler<T> protected constructor(
    private val subscribeOnScheduler: Scheduler,
    private val observeOnScheduler: Scheduler
) : ObservableTransformer<T, T>,
    SingleTransformer<T, T>,
    MaybeTransformer<T, T>,
    CompletableTransformer,
    FlowableTransformer<T, T> {

    override fun apply(upstream: Completable): CompletableSource {
        return upstream
            .subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream
            .subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return upstream
            .subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
            .subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream
            .subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
    }
}
