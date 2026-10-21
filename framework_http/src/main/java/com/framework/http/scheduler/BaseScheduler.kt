package com.framework.http.scheduler

import io.reactivex.rxjava3.core.*
import org.reactivestreams.Publisher

/**
 * 统一 RxJava 调度器基类。
 * 这里保留通用的 Transformer 逻辑，避免各具体调度器重复实现。
 * T 必须是 Any 的子类型，保证上下游泛型一致，不允许被错误收窄到 Any。
 */
abstract class BaseScheduler<T : Any> protected constructor(
    private val subscribeOnScheduler: Scheduler,
    private val observeOnScheduler: Scheduler
) : ObservableTransformer<T, T>,
    SingleTransformer<T, T>,
    MaybeTransformer<T, T>,
    CompletableTransformer,
    FlowableTransformer<T, T> {

    override fun apply(upstream: Completable): Completable {
        return upstream
            .subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream
            .subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Maybe<T>): Maybe<T> {
        return upstream
            .subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Observable<T>): Observable<T> {
        return upstream
            .subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Single<T>): Single<T> {
        return upstream
            .subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
    }
}
