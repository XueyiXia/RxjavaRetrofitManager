package com.framework.http.observer

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

/**
 * @author: xiaxueyi
 * @date: 2023-02-28
 * @time: 16:54
 * @说明: 创建观察者基类
 */

class BaseObserver<T : Any> :Observer<T>{

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: T) {

    }

    override fun onError(e: Throwable) {

    }

    override fun onComplete() {

    }
}