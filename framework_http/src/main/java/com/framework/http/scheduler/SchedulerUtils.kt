package com.framework.http.scheduler

/**
 * @author: xiaxueyi
 * @date: 2022-12-05
 * @time: 13:11
 * @说明: 调度器工厂入口，保留原有 API 兼容。
 */
object SchedulerUtils {

    fun <T> ioToMainScheduler(): IoMainScheduler<T> {
        return IoMainScheduler()
    }

    fun <T> newThreadScheduler(): NewThreadMainScheduler<T> {
        return NewThreadMainScheduler()
    }

    fun <T> singleMainScheduler(): SingleMainScheduler<T> {
        return SingleMainScheduler()
    }

    fun <T> computationScheduler(): ComputationMainScheduler<T> {
        return ComputationMainScheduler()
    }

    fun <T> trampolineMainScheduler(): TrampolineMainScheduler<T> {
        return TrampolineMainScheduler()
    }
}
