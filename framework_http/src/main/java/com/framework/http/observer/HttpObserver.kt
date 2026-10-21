package com.framework.http.observer

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.framework.http.callback.DownloadCallback
import com.framework.http.interfac.HttpResponseListener
import com.framework.http.manager.RxHttpTagManager
import com.framework.http.utils.TypeUtils
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.disposables.DisposableHelper
import io.reactivex.rxjava3.internal.util.EndConsumerHelper
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicReference

/**
 * @author: xiaxueyi
 * @date: 2023-02-28
 * @time: 16:54
 * @说明: 创建观察者基类
 */
class HttpObserver<T : Any> constructor(
    private val responseListener: HttpResponseListener<T>?,
    private val tag: Any?,
    lifecycleOwner: LifecycleOwner?
) : Observer<T>, Disposable, LifecycleEventObserver {

    companion object {
        private const val TAG = "HttpObserver"
    }

    private val upstream = AtomicReference<Disposable>()

    init {
        bindLifecycleOwner(lifecycleOwner)
    }

    override fun onSubscribe(d: Disposable) {
        if (EndConsumerHelper.setOnce(upstream, d, javaClass)) {
            if (tag != null) {
                RxHttpTagManager.getInstance().addTag(tag, d)
            } else {
                RxHttpTagManager.getInstance().addTag(RxHttpTagManager.generateRandomTag(), d)
            }
            onStart()
        }
    }

    override fun onNext(t: T) {
        val tagName = tag?.toString() ?: ""

        if (responseListener is DownloadCallback) {
            Log.e(TAG, "onNext--->> : DownloadCallback")
            responseListener.onSucceed(t, tagName)
            return
        }

        val genericType = responseListener?.type ?: TypeUtils.getType(responseListener?.javaClass ?: t::class.java)

        val parsedValue: Any? = when (genericType) {
            String::class.java -> t.toString()
            JSONObject::class.java -> if (t is String) JSONObject(t) else JSONObject(t.toString())
            JSONArray::class.java -> if (t is String) JSONArray(t) else JSONArray(t.toString())
            else -> {
                try {
                    Gson().fromJson(t.toString(), genericType)
                } catch (_: Exception) {
                    t
                }
            }
        }

        val result = parsedValue as? T
        if (result != null) {
            responseListener?.onSucceed(result, tagName)
        } else {
            responseListener?.onSucceed(t, tagName)
        }

        Log.e(TAG, "onNext--->> : $t")
    }

    override fun onError(e: Throwable) {
        responseListener?.onError(e)
        Log.e(TAG, "onError--->> : $e")
    }

    override fun onComplete() {
        responseListener?.onCompleted()
        Log.e(TAG, "onComplete--->>: ")
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

    private fun onStart() {
        responseListener?.onStart()
    }

    /**
     * 绑定生命周期
     * @param lifecycleOwner LifecycleOwner?
     */
    private fun bindLifecycleOwner(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Event) {
        Log.e(TAG, "source--->>: $source   event--->>: $event")
        when (event) {
            Event.ON_DESTROY -> {
                if (!TextUtils.isEmpty(tag.toString())) {
                    RxHttpTagManager.getInstance().removeTag(tag)
                } else {
                    RxHttpTagManager.getInstance().cancelTag(null)
                }
            }
            else -> {
                // no-op
            }
        }
    }
}
