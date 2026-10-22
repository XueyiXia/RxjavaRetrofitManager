package com.xueyixia.http.config

import java.io.File

/**
 * Download configuration.
 */
data class DownloadOptions(
    val url: String,
    val saveDir: File,
    val fileName: String,
    val overwrite: Boolean = true,
    val md5: String? = null
)
