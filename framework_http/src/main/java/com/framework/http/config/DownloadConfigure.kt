package com.framework.http.config

/**
 * @author: xiaxueyi
 * @date: 2023-03-01
 * @time: 15:57
 * @说明:
 */

class DownloadConfigure {

    companion object {
        /**
         * 保持兼容性，但不再复用全局单例状态，避免多任务并发时相互覆盖配置。
         */
        fun get(): DownloadConfigure = DownloadConfigure()
    }

    var directoryFile: String = ""
    var filename: String = ""
    var md5: String? = null
    var isBreakpoint = false
    var urlLink: String = ""

    /**
     *
     * @param directoryFile String
     * @return DownloadConfigure
     */
    fun setDirectoryFile(directoryFile: String): DownloadConfigure {
        this.directoryFile = directoryFile
        return this
    }

    /**
     *
     * @param fileName String
     * @return DownloadConfigure
     */
    fun setFileName(fileName: String): DownloadConfigure {
        this.filename = fileName
        return this
    }

    /**
     *
     * @param urlLink String
     * @return DownloadConfigure
     */
    fun setDownloadUrl(urlLink: String): DownloadConfigure {
        this.urlLink = urlLink
        return this
    }

    /**
     * 是否断点下载
     * @param breakpoint Boolean
     * @return DownloadConfigure
     */
    fun breakpoint(breakpoint: Boolean): DownloadConfigure {
        isBreakpoint = breakpoint
        return this
    }

    /**
     *
     * @param md5 String?
     * @return DownloadConfigure
     */
    fun setMd5(md5: String?): DownloadConfigure {
        this.md5 = md5
        return this
    }
}
