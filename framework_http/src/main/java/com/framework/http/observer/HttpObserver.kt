package com.framework.http.observer

import com.framework.http.RxHttpTagManager
import com.framework.http.interfac.SimpleResponseListener
import io.reactivex.rxjava3.internal.disposables.DisposableHelper
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.util.EndConsumerHelper
import java.util.concurrent.atomic.AtomicReference

/**
 * @author: xiaxueyi
 * @date: 2023-02-28
 * @time: 16:54
 * @说明: 创建观察者基类
 */

class HttpObserver<T : Any>(private val simpleResponseListener: SimpleResponseListener<T>?, private val tag: Any?) :Observer<T> , Disposable {

    private val upstream = AtomicReference<Disposable>()

    override fun onSubscribe(d: Disposable) {
        if (EndConsumerHelper.setOnce(upstream, d, javaClass)) {
            if (tag != null) {
                RxHttpTagManager.getInstance().addTag(tag, d)
            }
        }
    }

    override fun onNext(t: T) {
        simpleResponseListener?.onSucceed(t,tag.toString())
    }

    override fun onError(e: Throwable) {
        simpleResponseListener?.onError(e)
    }

    override fun onComplete() {
        simpleResponseListener?.onCompleted()
    }

    override fun dispose() {
        if (tag != null) {
            RxHttpTagManager.getInstance().removeTag(tag)
        }
        DisposableHelper.dispose(upstream)
    }

    override fun isDisposed(): Boolean {
        return upstream.get() === DisposableHelper.DISPOSED
    }
}