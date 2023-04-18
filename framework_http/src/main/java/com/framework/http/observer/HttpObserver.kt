package com.framework.http.observer

import android.util.Log
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.framework.http.interfac.SimpleResponseListener
import com.framework.http.manager.RxHttpTagManager
import com.framework.http.utils.ParseUtils
import com.framework.http.utils.TypeUtils
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.disposables.DisposableHelper
import io.reactivex.rxjava3.internal.util.EndConsumerHelper
import java.util.concurrent.atomic.AtomicReference

/**
 * @author: xiaxueyi
 * @date: 2023-02-28
 * @time: 16:54
 * @说明: 创建观察者基类
 */

class HttpObserver<T : Any> constructor(
    private val simpleResponseListener: SimpleResponseListener<T>?,
    private val tag: Any?,
    lifecycleOwner:LifecycleOwner? ) :Observer<T> , Disposable, LifecycleEventObserver {

    companion object{
        private const val TAG="HttpObserver"
    }

    private val upstream = AtomicReference<Disposable>()

    init {
        bindLifecycleOwner(lifecycleOwner)
    }

    override fun onSubscribe(d: Disposable) {
        if (EndConsumerHelper.setOnce(upstream, d, javaClass)) {
            if (tag != null) {
                RxHttpTagManager.getInstance().addTag(tag, d)
            }else{
                RxHttpTagManager.getInstance().addTag(RxHttpTagManager.generateRandomTag(), d)
            }
        }
    }

    override fun onNext(t: T) {
        val genericType = simpleResponseListener?.let {
            TypeUtils.getType(it.javaClass)
        }
        val result: T? = genericType?.let { ParseUtils.parseResponse(t.toString(), it) }

        result?.let {
            simpleResponseListener?.onSucceed(it,tag.toString())
        }

        Log.e(TAG,"onNext--->> : $t  ")
    }

    override fun onError(e: Throwable) {
        simpleResponseListener?.onError(e)
        Log.e(TAG,"onError--->> : $e  ")
    }

    override fun onComplete() {
        simpleResponseListener?.onCompleted()
        Log.e(TAG,"onComplete--->>: ")
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

    /**
     * 绑定生命周期
     * @param lifecycleOwner LifecycleOwner?
     */
    private fun bindLifecycleOwner(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Event) {
        Log.e(TAG,"source--->>: $source   event--->>: $event")
        when (event) {
            Event.ON_DESTROY -> {
                //取消网络请求,tag为null取消所有网络请求,tag不为null值，取消指定的网络请求
                RxHttpTagManager.getInstance().cancelTag(tag)
            }
            else -> {
                RxHttpTagManager.getInstance().cancelTag(null)
            }
        }
    }
}