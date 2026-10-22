package com.xueyixia.http.config

import java.io.File

/**
 * 下载相关配置。
 * 不再把处理逻辑和文件地址混在一起，统一使用一个对象表达下载意图。
 */
data class DownloadOptions(
    val url: String,
    val saveDir: File,
    val fileName: String,
    val overwrite: Boolean = true,
    val md5: String? = null
)
