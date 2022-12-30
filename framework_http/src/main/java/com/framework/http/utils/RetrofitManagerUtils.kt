package com.framework.http.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 15:20
 * @说明:
 */

class RetrofitManagerUtils private constructor() {

    private lateinit var mRetrofit: Retrofit

    companion object{
        private const val mDateFormat:String="yyyy-MM-dd HH:mm:ss";
        val mInstance : RetrofitManagerUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            RetrofitManagerUtils()
        }
    }




    fun init(baseUrl:String){



        //创建json
        val gson: Gson = GsonBuilder()
            .setDateFormat(mDateFormat)
            .serializeNulls()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();


        /**
         * Retrofit
         */
        mRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClientUtils.getOkHttpClientBuild())
            .build()
    }


    /**
     *
     * @param tClass Class<T>
     * @return T
     */
    fun <T> createService(tClass: Class<T>):T{
        return mRetrofit.create(tClass)
    }
}