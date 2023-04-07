package com.framework.http.config

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.telephony.mbms.DownloadRequest
import androidx.fragment.app.Fragment
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author: xiaxueyi
 * @date: 2023-03-01
 * @time: 15:57
 * @说明:
 */

class DownloadConfigure {

    private object Config {
        val downloadConfigure = DownloadConfigure()
    }

    companion object {
        fun get(): DownloadConfigure {
            return Config.downloadConfigure
        }
    }

    var dir: String = ""
    var filename: String = ""
    var md5: String? = null
    var isBreakpoint = false



    fun dir(dir: String): DownloadConfigure {
        this.dir = dir
        return this
    }

    fun filename(filename: String): DownloadConfigure {
        this.filename = filename
        return this
    }

    fun breakpoint(breakpoint: Boolean): DownloadConfigure {
        isBreakpoint = breakpoint
        return this
    }

    fun md5(md5: String?): DownloadConfigure {
        this.md5 = md5
        return this
    }
}