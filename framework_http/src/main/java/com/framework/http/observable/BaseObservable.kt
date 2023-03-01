package com.framework.http.observable

import com.framework.http.observer.BaseObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * @author: xiaxueyi
 * @date: 2023-02-28
 * @time: 16:57
 * @说明: 创建被观察者基类
 */

class BaseObservable{

    private var mObserver:BaseObserver<Any> = BaseObserver<Any>()

    fun observe() {

    }

}