package com.framework.http.observable

import com.framework.http.observer.HttpObserver


/**
 * @author: xiaxueyi
 * @date: 2023-02-28
 * @time: 16:57
 * @说明: 创建被观察者基类
 */

class BaseObservable{

    private var mObserver:HttpObserver<Any> = HttpObserver<Any>()

    fun observe() {

    }

}