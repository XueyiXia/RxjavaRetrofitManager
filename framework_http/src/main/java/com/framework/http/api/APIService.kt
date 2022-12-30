package com.framework.http.api

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 15:40
 * @说明:
 */
interface APIService {


    /**
     *
     * @param url String?
     * @param parameter MutableMap<String, Any>?
     * @param header MutableMap<String, Any>?
     * @return Observable<*>
     */
    @GET
    fun get(@Url url: String?, @QueryMap parameter: MutableMap<String, Any>?, @HeaderMap header: MutableMap<String, Any>?):Observable<*>
}