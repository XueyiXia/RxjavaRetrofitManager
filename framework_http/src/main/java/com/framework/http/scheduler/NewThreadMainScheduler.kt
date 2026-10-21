package com.framework.http.scheduler

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

open class NewThreadMainScheduler<T : Any> : BaseScheduler<T>(
    Schedulers.newThread(),
    AndroidSchedulers.mainThread()
)
