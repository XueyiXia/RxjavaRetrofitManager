package com.rxjava_retrofit.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.framework.http.service.DownloadService
import com.rxjava_retrofit.HttpApi
import com.rxjava_retrofit.R

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class UserFragment :Fragment(){

    private var mRootView: View? = null

    private lateinit var mBtnDownload: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_user, container, false)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBtnDownload=view.findViewById(R.id.download)

        mBtnDownload.setOnClickListener {
            val intent= Intent(context, DownloadService::class.java)
            intent.putExtra("url", HttpApi.URL_DOWNLOAD)
            context?.startService(intent)
        }
    }
}