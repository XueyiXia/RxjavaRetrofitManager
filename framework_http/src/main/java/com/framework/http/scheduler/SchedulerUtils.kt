package com.framework.http.scheduler

/**
 * @author: xiaxueyi
 * @date: 2022-12-05
 * @time: 13:11
 * @说明: 调度器工厂入口，保留原有 API 兼容。
 */
object SchedulerUtils {

    fun <T : Any> ioToMainScheduler(): IoMainScheduler<T> {
        return IoMainScheduler()
    }

    fun <T : Any> newThreadScheduler(): NewThreadMainScheduler<T> {
        return NewThreadMainScheduler()
    }

    fun <T : Any> singleMainScheduler(): SingleMainScheduler<T> {
        return SingleMainScheduler()
    }

    fun <T : Any> computationScheduler(): ComputationMainScheduler<T> {
        return ComputationMainScheduler()
    }

    fun <T : Any> trampolineMainScheduler(): TrampolineMainScheduler<T> {
        return TrampolineMainScheduler()
    }
}
