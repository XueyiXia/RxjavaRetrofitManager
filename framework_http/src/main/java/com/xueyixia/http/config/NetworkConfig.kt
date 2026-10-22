package com.xueyixia.http.config

/**
 * 真实项目里，基础路径和全局参数需要统一入口，不能散落在类和函数中。
 */
object ApiConstants {
    const val BASE_URL = "https://api.example.com/"
    const val FEED_PATH = "v2/feed"
    const val UPLOAD_IMAGE_URL = "/upload/image"
    const val APK_DOWNLOAD_URL = "https://example.com/app-release.apk"
    const val SECONDARY_APK_DOWNLOAD_URL = "https://example.com/app-release-2.apk"
}
