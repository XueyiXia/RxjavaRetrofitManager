package com.framework.http.scheduler

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class ComputationMainScheduler<T : Any> : BaseScheduler<T>(
    Schedulers.computation(),
    AndroidSchedulers.mainThread()
)
