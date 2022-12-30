package com.framework.http.exception

import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import javax.net.ssl.SSLHandshakeException



/**
 * @author: xiaxueyi
 * @date: 2022-08-18
 * @time: 15:28
 * @说明: 错误/异常处理工具
 */
object ExceptionEngine {
    public val code_error_unknown: Any = -1000000 //未知错误
    public val code_error_analytic: Any = -1000001 //解析数据错误
    private val code_error_connect: Any = -1000002 //网络连接错误
    private val code_error_time_out: Any = -1000003 //网络连接超时
    private  const val msg_error_unknown = "未知错误" //未知错误
    public const val msg_error_analytic = "解析错误" //解析数据错误
    private const val msg_error_connect = "连接失败" //网络连接错误
    private const val msg_error_time_out = "网络超时" //网络连接超时
    private const val msg_error_http = "网络错误" //网络错误
    fun handleException(e: Throwable?): EventException {
        val ex: EventException
        return if (e is HttpException) { //HTTP错误(均视为网络错误)
            ex = EventException(
                e,
                e.code(),
                msg_error_http
            )
            ex
        } else if (e is JsonParseException || e is JSONException
            || e is ParseException || e is MalformedJsonException
        ) {  //解析数据错误
            ex = EventException(
                e,
                code_error_analytic,
                msg_error_analytic
            )
            ex
        } else if (e is ConnectException || e is SSLHandshakeException || e is UnknownHostException) { //连接网络错误
            ex = EventException(
                e,
                code_error_connect,
                msg_error_connect
            )
            ex
        } else if (e is SocketTimeoutException) { //网络超时
            ex = EventException(
                e,
                code_error_time_out,
                msg_error_time_out
            )
            ex
        } else {  //未知错误
            ex = EventException(
                e,
                code_error_unknown,
                msg_error_unknown
            )
            ex
        }
    }
}