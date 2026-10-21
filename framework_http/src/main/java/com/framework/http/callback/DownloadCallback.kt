package com.framework.http.callback

import com.framework.http.interfac.HttpResponseListener

open class DownloadCallback<T> : HttpResponseListener<T>() {
    override fun onNext(response: T?) {
        // 默认实现为空，避免强制子类必须实现，不影响真实回调。 
    }

    override fun onError(e: Throwable?) {
        // 默认实现为空，避免强制子类必须实现，不影响真实回调。
    }
}
