package com.framework.http.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author: xiaxueyi
 * @date: 2023-02-22
 * @time: 14:02
 * @说明:
 */
object TypeUtils {

    /**
     * 安全解析泛型参数，优先查找父类/接口中的真实泛型类型。
     */
    fun getType(subclass: Class<*>): Type {
        var currentClass: Class<*>? = subclass
        while (currentClass != null && currentClass != Any::class.java) {
            currentClass.genericSuperclass?.let { genericSuperclass ->
                if (genericSuperclass is ParameterizedType) {
                    val typeArguments = genericSuperclass.actualTypeArguments
                    if (typeArguments.isNotEmpty()) {
                        return typeArguments[0]
                    }
                }
            }

            for (genericInterface in currentClass.genericInterfaces) {
                if (genericInterface is ParameterizedType) {
                    val typeArguments = genericInterface.actualTypeArguments
                    if (typeArguments.isNotEmpty()) {
                        return typeArguments[0]
                    }
                }
            }

            currentClass = currentClass.genericSuperclass as? Class<*>
        }
        return subclass
    }

    /**
     * 兼容历史调用方，返回泛型 Type。
     */
    fun <T> getGenericType(subclass: Class<*>): Type {
        return getType(subclass)
    }
}
