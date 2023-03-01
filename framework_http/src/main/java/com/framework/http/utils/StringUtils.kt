package com.framework.http.utils

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/**
 * @author: xiaxueyi
 * @date: 2023-02-22
 * @time: 14:02
 * @说明:
 */
object StringUtils {


    /**
     * 从Assets读取JSON文件
     * @param context
     * @param fileName
     * @return
     */
    fun getAssetsFile(context: Context, fileName: String?): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(
                InputStreamReader(
                    assetManager.open(
                        fileName!!
                    )
                )
            )
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }


    /**
     * 获取Raw文件数据
     * @param context
     * @param fileName
     * @return
     */
    fun getRawFile(context: Context, fileName: Int): String {
        val stringBuilder = StringBuilder()
        try {
            val inputStream = context.resources.openRawResource(fileName)
            val isr = InputStreamReader(inputStream, "UTF-8")
            val br = BufferedReader(isr)
            var line: String? = ""
            while (br.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

}