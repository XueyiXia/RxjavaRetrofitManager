package com.framework.http.utils

import android.text.TextUtils
import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * 统一 Gson 工具类，提供正式版稳定封装。
 */
object GsonUtils {

    private val gson = Gson()

    fun toJson(obj: Any?): String {
        if (obj == null) return ""
        val raw = obj.toString()
        if (TextUtils.isEmpty(raw)) return ""
        return try {
            gson.toJson(obj)
        } catch (_: Exception) {
            raw
        }
    }

    fun <T> fromJson(str: String?, type: Type): T? {
        if (str.isNullOrEmpty()) return null
        return try {
            gson.fromJson(str, type)
        } catch (_: Exception) {
            null
        }
    }
}
