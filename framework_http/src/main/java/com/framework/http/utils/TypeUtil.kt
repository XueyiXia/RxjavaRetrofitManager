package com.framework.http.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
/**
 * @author: xiaxueyi
 * @date: 2023-02-22
 * @time: 14:02
 * @说明:
 */
object TypeUtil {

    fun getType(subclass: Class<*>): Type {
        val superclass = subclass.genericSuperclass
        return if (superclass is Class<*>) {
            subclass
        } else {
            val parameterized = superclass as ParameterizedType
            parameterized.actualTypeArguments[0]
        }
    }
}