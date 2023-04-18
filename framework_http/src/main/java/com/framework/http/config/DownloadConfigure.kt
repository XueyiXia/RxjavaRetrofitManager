package com.framework.http.config

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

    var directoryFile: String = ""
    var filename: String = ""
    var md5: String? = null
    var isBreakpoint = false


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
     * @return String
     */
//    fun getDirectoryFile()=this.directoryFile

    /**
     *
     * @param filename String
     * @return DownloadConfigure
     */
    fun filename(filename: String): DownloadConfigure {
        this.filename = filename
        return this
    }

    /**
     *
     * @return String
     */
//    fun getFileName()=this.filename


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
     * @return Boolean
     */
//    fun getIsBreakpoint()=this.isBreakpoint


    /**
     *
     * @param md5 String?
     * @return DownloadConfigure
     */
    fun md5(md5: String?): DownloadConfigure {
        this.md5 = md5
        return this
    }

    /**
     *
     * @return String?
     */
//    fun getMd5()=md5
}